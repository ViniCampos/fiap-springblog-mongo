# Projeto de integração JPA com MongoDB

## Sumário
1. [Sobre o Projeto](#projeto-de-integração-jpa-com-mongodb)
2. [Documentação sobre o Mongo](#documentação-sobre-mongo)
3. [Instalação com Docker](#instalação-do-mongo-para-usardocker)
4. [Uso do Shell e Comandos Úteis](#uso-do-shell)
5. [Testando Transações](#como-testar-transações)
6. [Backup e Restore do Mongo](#fazendo-backup-and-restore-o-mongo)
7. [Criando Replica Set e Sharding](#passos-para-criar-a-replicação-do-mongo-em-nós)

## Documentação sobre mongo
#### https://spring.io/projects/spring-data-mongodb  

## Instalação do mongo para usar(docker)
### Baixar e rodar ou apenas rodar 
Instalar: docker run --name meu_mongo -d -p 27017:27017 mongo  
Iniciar: docker start meu_mongo  
Stopar: docker stop meu_mongo  
Valiar: docker ps  
  
OBS: Pode-se utilizar o MongoCompass para melhor visualização dos dados  
https://www.mongodb.com/products/tools/compass

### Uso do shell
Caso esteja utilizando com docker, ele já vem com o shell na imagem. Do contrário, necessário baixar o shell separado  
Utilizar o shell: docker exec -it meu_mongo mongosh
Acessar console do container: docker exec -it meu_mongo bash

#### Comandos utéis: 
show dbs  
use workshop  
db.createCollection("customers")  
show collections  
db.customers.countDocuments()  
db.customers.insertOne({nome:"Vini", idade:28})  
db.customers.find()  
db.customers.find({}, {"_id":0}) //EXIBE TODOS DOCS E SOME COM _ID  
db.customers.find().pretty()  


### Como testar transações
Usando o documento já criado com este ID: 6768234247eb735394889b7c  
curl --request PUT \  
--url http://localhost:8080/artigos \  
--header 'Content-Type: application/json' \  
--data '	{  
"codigo": "6768234247eb735394889b7c", 
"titulo": "Artigo Atualizado 3",  
"data": "2024-12-10T12:34:56",  
"texto": "Texto do artigo.",  
"url": "http://exemplo.com",  
"status": 1,  
"autor": { 
 "codigo": "6758de192d57e11cf63e5dd0" },  
"version": 0  
}'

## FAZENDO BACKUP AND RESTORE O MONGO
### Caso Local
Instalar https://www.mongodb.com/try/download/database-tools  
Copiar para mongodb/server/{version}/bin  
mongodump --out={Seu Path}  
mongorestore --nsInclude=blog.* --host=localhost --port=27017 "{path_do_backup}"

### Caso docker
-- docker exec -it meu_mongo mongodump --out={PATH}  
-- docker exec -it meu_mongo mongorestore --drop --nsInclude nome_do_banco /dump/nome_do_banco  
  
OBS - Para salvar os dados na máquina local (fora do container), será necessário mapear a pasta na criação do banco, após isso os arquivos serão salvos corretamente  
-- docker run -d --name meu_mongo -v C:\Users\Vinic\mongodb:/backup mongo  

### Fazendo test com docker com imagem local  
#### Criando backup  
Roda comando criando conexão entre pasta local e container  
-- docker run -d --name meu_mongo -v C:\Users\Vinic\mongodb\backup:/blog -p 27017:27017 mongo

Roda comando criando realizando backup da pasta no HD da máquina (não container)  
-- docker exec -it meu_mongo mongodump --out=/backup

Deletando DB  
-- docker exec -it meu_mongo2 mongosh
-- use test
-- db.dropDatabase()

Recriando DB  
-- docker exec -it meu_mongo mongorestore --drop /backup
-- OU docker exec -it meu_mongo mongorestore --drop /backup.*

#### OBS - Copiando arquivo do container para máquina local
docker cp meu_mongo:/data/backup C:\Users\Vinic\mongodb


## PASSOS PARA CRIAR A REPLICAÇÃO DO MONGO EM NÓS
**1 - Criar 3 pastas de config e 2 pastas de shard**  
mkdir C:\data\db\configdb{NUMERO_DO_CONFIG}  
mkdir C:\data\db\shard{NUMERO_DO_SHARD}  

**2 - Criar configuradores (Criar um em cada pronpt)**  
mongod --configsvr --port 28041 --bind_ip localhost --replSet  config_repl --dbpath C:\data\db\configdb1  
mongod --configsvr --port 28042 --bind_ip localhost --replSet  config_repl --dbpath C:\data\db\configdb2  
mongod --configsvr --port 28043 --bind_ip localhost --replSet  config_repl --dbpath C:\data\db\configdb3  

**3 - Configurar o conjunto de regras (Novo pronpt)**  //A PARTIR DAQUI TEMOS 3 REPLICADORES DE DADOS  
mongosh --host localhost --port 28041  
rsconf = 
```json
rsconf = {	
  _id: "config_repl",  
  members: [
    {_id:0,host: "localhost:28041"},
    {_id:1,host: "localhost:28042"},
    {_id:2,host: "localhost:28043"}
  ]
}
```
rs.initiate(rsconf)  
rs.status(); //Ver status dos configuradores


**4 - Criar os shards (Fragmentação dos dados - Novo pronpt)**  
mongod --shardsvr --port 28081 --bind_ip localhost --replSet shard_repl1 --dbpath C:\data\db\shard1  

**5 - Entrar no shard e ativar (Novo pronpt)**  
mongosh --host localhost --port 28081  
```json 
rsconf = {_id: "shard_repl1",members: [{_id:0,host: "localhost:28081"}]}
```  
rs.initiate(reconf)  
rs.status()
show dbs //JÁ DEVE VER AQUI A REPLICAÇÃO PARA ESSE SHARD (NOVO BANCO)  

**6 - Repetir para criar o novo shard**  

**7 - Criar servidor de roteamento (Máquina proxy - Novo pronpt)**
**//EXECUTA mongoS expondo em uma nova porta o servidor e setando o config 1 como nó principal de configuração**  
mongos --configdb config_repl/localhost:28041 --bind_ip localhost --port 27040  

**8 - Se conectar so servidor MONGOS e adicionar shards (novo pronpt)**  
mongosh --hot localhost --port 27040  
sh.addShard("shard_repl1/localhost:28081");  
sh.addShard("shard_repl2/localhost:28082");  

**9 - No mesmo pronpt de HOLDER habilitar os dbs para ocorrer o sharding e particionar as collections**
sh.enableSharding("blog");  
sh.shardCollection("blog.artigo", {"_id": 1});  
sh.shardCollection("blog.autor", {"_id": 1});  
sh.shardCollection("blog.categoria", {"_id": 1});  
sh.status();  

**10 - Ativar balanceamento**  
sh.startBalancer();  
sh.status();  
sh.balancerCollectionStatus("blog.artigo"); //Olhar mais especificamente uma collection no shard 

