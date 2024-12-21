DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS groups;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  city      CHAR REFERENCES cities
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE cities (
    id        CHAR PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TYPE group_type AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE TABLE groups (
    id CHAR PRIMARY KEY,
    user_id INTEGER references users,
    project_name TEXT references projects,
    name TEXT NOT NULL UNIQUE,
    type group_type NOT NULL
);

CREATE TABLE projects (
    name TEXT PRIMARY KEY,
    description TEXT
)