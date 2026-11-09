create database bd_ventas;

use bd_ventas;

create table tb_cargo (
    codigo_cargo char(5) not null primary key,
    cargo varchar(30) not null);

create table tb_personal (
    dni char(8) not null primary key,
    ap_paterno varchar(25) not null,
    ap_materno varchar(25) not null,
    nombre varchar(25) not null,
    genero char(1) not null,
    fecha_nacimiento date,
    sueldo float,
    codigo_cargo char(5) not null,
    foreign key (codigo_cargo) references tb_cargo (codigo_cargo));

insert into tb_cargo values
('C0001', 'Empleado'),
('C0002', 'Analista de Sistemas'),
('C0003', 'Operador');

select * from tb_cargo;

insert into tb_personal values
('12345678', 'Castillo', 'Liñán', 'Joe', 'M', '2000-03-15', 1592.74, 'C0002'),
('11223344', 'Ramos', 'López', 'Ericka', 'F', '2002-11-20', 1756.96, 'C0001'),
('66554422', 'Flores', 'Alvarez', 'Rosa', 'F', '1999-06-30', 2159.47, 'C0002');

select * from tb_personal;

-- -----------------------------------------
delimiter $$
create procedure sp_listar_personal()
begin
    select * from tb_personal order by ap_paterno asc;
end $$

call sp_listar_personal();

-- drop procedure sp_listar_personal;

-- -----------------------------------------
delimiter $$
create procedure sp_listar_personal_completo()
begin
    select tbp.dni as 'Núm. DNI',
           concat(tbp.ap_paterno, ' ', tbp.ap_materno, ' ', tbp.nombre) as 'Apellidos y Nombre',
           tbc.cargo as 'Cargo '
    from tb_personal tbp inner join tb_cargo tbc on
    (tbp.codigo_cargo = tbc.codigo_cargo)
    order by tbc.cargo asc, tbp.ap_paterno asc;
end $$

call sp_listar_personal_completo();

-- drop procedure sp_listar_personal_completo;

-- -----------------------------------------
delimiter $$
create procedure sp_buscar_personal(in valor char(8))
begin
    select * from tb_personal where dni = valor;
end $$

call sp_buscar_personal('12345678');

-- drop procedure sp_buscar_personal;

-- -----------------------------------------
delimiter $$
create procedure sp_registrar_personal(
	in nd char(8), in app varchar(25), in apm varchar(25), in nom varchar(25),
    in gen char(1), in fnac date, in sue float, cod_car char(5))
begin
    insert into tb_personal values (nd, app, apm, nom, gen, fnac, sue, cod_car);
end $$

call sp_registrar_personal('25896314', 'Villa', 'Mendoza', 'Roberto', 'M', '1999-10-20', 1596.44, 'C0002');

-- drop procedure sp_registrar_personal;

-- -----------------------------------------
delimiter $$
create procedure sp_listar_cargo()
begin
    select * from tb_cargo order by cargo asc;
end $$

call sp_listar_cargo();

-- drop procedure sp_listar_cargo;
