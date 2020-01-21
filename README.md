1) Clone this repository
2) Add correct git user and git token to application.propperties.
    How to create token see: 
    https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line


    github.user=<git_user>
    github.token=<git_tocken>
2) Run 


    docker-compose up
3) For getting top 10 repositories by number of stars run:


    curl --location --request POST 'http://localhost:8080/graphql' \
    --data-raw '{
        "query":"{findTopRepositories {id, stars, createdAt } }"
    }'