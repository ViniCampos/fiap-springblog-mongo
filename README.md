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