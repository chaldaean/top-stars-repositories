1) Clone this repository
2) Add correct personal git access token to `\top-stars-repositories\repos_collector\src\main\resources\application.properties`.
    How to create personal access tokens see: 
    https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line
    ```
    github.token=<your_git_token>
   ```
3) Run docker-compose
    ```
    docker-compose up
   ```
4) For getting top 10 repositories by number of stars run:
    ```
    curl --location --request POST 'http://localhost:8080/graphql' \
    --data-raw '{
        "query":"{findTopRepositories {id, stars, createdAt } }"
    }'
   ```
   
5) If you need to create database and DB user localy - run script `create_schema.sql`