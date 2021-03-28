CREATE TABLE tb_sender (
  id_sender int(11) NOT NULL AUTO_INCREMENT,
  name varchar(60) DEFAULT NULL,
  PRIMARY KEY (id_sender)
);

CREATE TABLE tb_consignee (
  id_consignee int(11) NOT NULL AUTO_INCREMENT,
  name varchar(60) DEFAULT NULL,
  person_identifier varchar(60) DEFAULT NULL,
  type_of_person_identifier ENUM('CPF','CNPJ') DEFAULT NULL,
  PRIMARY KEY (id_consignee)
);

CREATE TABLE tb_localization (
  id_localization int(11) NOT NULL AUTO_INCREMENT,
  country varchar(60) DEFAULT NULL,
  state varchar(60) DEFAULT NULL,
  city varchar(60) DEFAULT NULL,
  PRIMARY KEY (id_localization)
);

CREATE TABLE tb_package_delivery (
  id_package_delivery int(11) NOT NULL AUTO_INCREMENT,
  weight double NOT NULL,
  id_localization int(11) NOT NULL,
  id_consignee int(11) NOT NULL,
  id_sender int(11) NOT NULL,
  PRIMARY KEY (id_package_delivery),
  FOREIGN KEY (id_localization) REFERENCES tb_localization (id_localization),
  FOREIGN KEY (id_consignee) REFERENCES tb_consignee (id_consignee),
  FOREIGN KEY (id_sender) REFERENCES tb_sender (id_sender)
  );

CREATE TABLE tb_letter_delivery (
  id_letter_delivery int(11) NOT NULL AUTO_INCREMENT,
  envelope boolean NOT NULL,
  id_localization int(11) NOT NULL,
  id_consignee int(11) NOT NULL,
  id_sender int(11) NOT NULL,
  PRIMARY KEY (id_letter_delivery),
  FOREIGN KEY (id_localization) REFERENCES tb_localization (id_localization),
  FOREIGN KEY (id_consignee) REFERENCES tb_consignee (id_consignee),
  FOREIGN KEY (id_sender) REFERENCES tb_sender (id_sender)
  );

INSERT INTO tb_sender (Name) VALUES 
  ('Magazine Luíza'),
  ('Casas Bahia'),
  ('Samsung Brasil'),
  ('Amazon');

INSERT INTO tb_consignee (name, person_identifier, type_of_person_identifier) VALUES 
  ('João da Silva','123.456.789-00','CPF'),
  ('Maria Green','123.456.789-01','CPF'),
  ('Philipe Moura','123.456.789-02','CPF'),
  ('Mercadinho Esquina','12.345.678/9012-00','CNPJ');

INSERT INTO tb_localization (country, state, city) VALUES 
  ('BRA','MT','Sinop'),
  ('BRA','DF','Brasília'),
  ('BRA','PR','Curitiba'),
  ('BRA','SP','Santos');

INSERT INTO tb_package_delivery (weight, id_localization, id_consignee, id_sender) VALUES 
  (10.5,1,1,1),
  (4.45,2,2,2);

INSERT INTO tb_letter_delivery (envelope, id_localization, id_consignee, id_sender) VALUES 
  (false,3,3,3),
  (true,4,4,4);