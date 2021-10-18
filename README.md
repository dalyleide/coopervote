# Coopervote
Sistema para controle de pauta e respectiva votação 

## Environment Variable
MONGO_URI

You can create the database in https://www.mongodb.com/cloud/atlas

```bash
MONGO_URI = mongodb+srv://root:<password>@coopervote.y4jkr.mongodb.net/myFirstDatabase?retryWrites=true&w=majority
```
>Replace <password> with the password for the root user. Replace myFirstDatabase with the name of the database that connections will use by default

## Runnig

### 1. Rabbitmq
Inside the folder `rabbitmq`

Build the image:
```bash
docker build -t rabbitmq-custom .
```

Run the docker composer:
```bash
docker-compose up
```
### 2. Discovery
Run ApplicationDiscoverApplication

### 3. Coopervote
Run CoopervoteApplication

### 4. Gatway
Run ApplicationGatewayApplication

## License
[MIT](https://choosealicense.com/licenses/mit/)
