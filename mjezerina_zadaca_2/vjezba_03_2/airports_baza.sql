#  MySQL

DROP TABLE IF EXISTS `airports`;
CREATE TABLE `airports` (
  `ident` varchar(10) NOT NULL,
  `type` varchar(30) NOT NULL,
  `name` varchar(255) NOT NULL,
  `elevation_ft` varchar(10) NULL,
  `continent` varchar(30) NULL,  
  `iso_country` varchar(30) NULL,  
  `iso_region` varchar(10) NULL,  
  `municipality` varchar(30) NULL,  
  `gps_code` varchar(10) NOT NULL,  
  `iata_code` varchar(10) NOT NULL,  
  `local_code` varchar(10) NOT NULL,  
  `coordinates` varchar(30) NOT NULL,  
  PRIMARY KEY (`ident`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#  Java DB

DROP TABLE airports;
CREATE TABLE airports (
  ident varchar(10) NOT NULL,
  type varchar(30) NOT NULL,
  name varchar(255) NOT NULL,
  elevation_ft varchar(10),
  continent varchar(30),  
  iso_country varchar(30),  
  iso_region varchar(10),  
  municipality varchar(30),  
  gps_code varchar(10) NOT NULL,  
  iata_code varchar(10) NOT NULL,  
  local_code varchar(10) NOT NULL,  
  coordinates varchar(30) NOT NULL,  
  PRIMARY KEY (ident)
);