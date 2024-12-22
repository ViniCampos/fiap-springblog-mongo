# fiap-springblog-mongo

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
mongorestore --nsInclude=blog.* --host=localhost --port=27017 "{path_do_backup}

## Caso docker
-- docker exec -it meu_mongo mongodump --out={PATH}  
-- docker exec -it meu_mongo mongorestore --drop --db nome_do_banco /dump/nome_do_banco  
  
OBS - Para salvar os dados na máquina local (fora do container), será necessário mapear a pasta na criação do banco, após isso os arquivos serão salvos corretamente  
-- docker run -d --name meu_mongo -v C:\Users\Vinic\mongodb:/backup mongo  

### Fazendo test com docker com imagem local  
#### Criando backup  
Roda comando criando conexão entre pasta local e container  
-- docker run -d --name meu_mongo -v C:\Users\Vinic\mongodb:/backup mongo  

Roda comando criando conexão entre pasta local e container  
-- docker exec -it meu_mongo mongodump --out=/backup

Deletando DB  
-- docker exec -it meu_mongo2 mongosh
-- use test
-- db.dropDatabase()

Recriando DB  
-- docker exec -it meu_mongo mongorestore --drop /backup

#### OBS - Copiando arquivo do container para máquina local
docker cp meu_mongo:/data/backup C:\Users\Vinic\mongodb
