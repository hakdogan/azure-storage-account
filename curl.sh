cd $(dirname $0)

curl -v -F "file=@./upload.png" -F container=${yourContainerName} -F blob=upload.png http://localhost:8080/api/upload

curl -v http://localhost:8080/api/access/${yourContainerName}/upload.png/30/Minutes
