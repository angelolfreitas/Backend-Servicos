baixe o docker no seu computador.
tenha alguma ide e copie esse projeto (preferencialmente o intellij)
inicie o docker desktop e rode o seguinte comando com o terminal no projeto copiado:

docker compose up -d
docker compose ps

isso vai demorar bastante no começo porque vai baixar todos os recursos que seu computador 
nao tem.
depois de um tempo, a api vai estar funcionando e voce pode testar cocm postman ou insomnia.
para encerrar os processos e apagar tudo, rode o seguinte comando:

docker compose pull
docker compose up -d

as variavies estao expostas, mas depois resolvemos isso.