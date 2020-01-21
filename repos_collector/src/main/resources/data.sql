CREATE TABLE IF NOT EXISTS Repository (
  id INT PRIMARY KEY,
  url VARCHAR(250) NOT NULL,
  name VARCHAR(250) NOT NULL,
  stargazers_count INT DEFAULT 0,
  created_at TIMESTAMP
);