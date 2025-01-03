DROP TABLE IF EXISTS users CASCADE ;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS projects;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE cities (
                        id   TEXT PRIMARY KEY,
                        name TEXT NOT NULL
);

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  city      CHAR REFERENCES cities
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TYPE group_type AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE TABLE projects (
                          name TEXT PRIMARY KEY,
                          description TEXT
);

CREATE TABLE groups (
    id CHAR PRIMARY KEY,
    user_id INTEGER references users,
    project_name TEXT references projects,
    name TEXT NOT NULL UNIQUE,
    type group_type NOT NULL
);