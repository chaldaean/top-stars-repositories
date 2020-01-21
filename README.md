1) Clone this repository
2) Add correct git user and git token to \top-stars-repositories\repos_collector\src\main\resources\application.properties.
    How to create token see: 
    https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line


    github.user=<git_user>

    
    github.token=<git_tocken>

3) To create database and user run script create_schema.sql

2) Run docker-compose


    docker-compose up
3) For getting top 10 repositories by number of stars run:


    curl --location --request POST 'http://localhost:8080/graphql' \
    --data-raw '{
        "query":"{findTopRepositories {id, stars, createdAt } }"
    }'