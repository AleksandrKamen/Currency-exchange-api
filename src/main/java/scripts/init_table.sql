create table if not exists currencies(
id INTEGER primary key autoincrement ,
code varchar(3) not null unique,
fullName varchar(50) not null unique,
sign varchar(3) not null unique
);

create table if not exists exchangerates(
id INTEGER primary key  autoincrement ,
baseCurrencyId int references currencies (id) not null,
targetCurrencyId int references currencies (id) not null,
rate Decimal(6) not null,
UNIQUE (baseCurrencyId, targetCurrencyId)
);