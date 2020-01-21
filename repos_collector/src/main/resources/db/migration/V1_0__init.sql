USE `app`;

DROP TABLE IF EXISTS `Repository`;
CREATE TABLE `Repository`
(
    id               BIGINT PRIMARY KEY,
    url              VARCHAR(250) NOT NULL,
    name             VARCHAR(250) NOT NULL,
    stargazers_count INT DEFAULT 0,
    created_at       TIMESTAMP,
    INDEX idx_stargazers_count (stargazers_count DESC)
);

