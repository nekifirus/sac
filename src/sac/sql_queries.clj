(ns sac.sql-queries)

(def create-tables "
create table if not exists articles (
  title varchar(255),
  author varchar(255),
  date varchar(64),
  doi varchar(32));
create table if not exists customers (
  email varchar(255) primary key,
  name varchar(255),
  password varchar(255)
)")

(def drop-tables "drop table articles, customers")

(def insert-customer "insert into customers (email, name, password) values(?, ?, ?)")

(def select-customer "select email, name from customers where email=?")

(def select-password "select password from customers where email=?")

(def insert-article "insert into articles (title, author, date, doi) values(?, ?, ?, ?)")
(def select-articles "select title, author, date, doi from articles limit ? offset ?")

(def count-articles "select count(*) from articles")
