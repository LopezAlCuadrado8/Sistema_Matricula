-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 08-11-2026 a las 16:52:16
-- Versión del servidor: 10.11.16-MariaDB
-- Versión de PHP: 8.4.27

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `matriculas_db`
--
CREATE DATABASE IF NOT EXISTS `matriculas_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `matriculas_db`;

DELIMITER $$
--
-- Procedimientos
--
DROP PROCEDURE IF EXISTS `sp_actualizar_curso`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_curso` (IN `p_id_curso` INT, IN `p_nombre` VARCHAR(60), IN `p_creditos` INT, IN `p_horas` INT, IN `p_id_carrera` INT)   BEGIN
    UPDATE tb_curso
    SET
        nombre = p_nombre,
        creditos = p_creditos,
        horas = p_horas,
        id_carrera = p_id_carrera
    WHERE id_curso = p_id_curso;
END$$

DROP PROCEDURE IF EXISTS `sp_actualizar_pago`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_pago` (IN `p_id_pago` INT, IN `p_monto` DECIMAL(10,2), IN `p_fecha` DATE, IN `p_metodo` VARCHAR(30))   BEGIN
    UPDATE tb_pago
    SET
        monto = p_monto,
        fecha = p_fecha,
        metodo = p_metodo
    WHERE id_pago = p_id_pago;
END$$

DROP PROCEDURE IF EXISTS `sp_actualizar_usuario`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_usuario` (IN `p_id_usuario` INT, IN `p_dni` CHAR(8), IN `p_nombres` VARCHAR(50), IN `p_apellidos` VARCHAR(50), IN `p_correo` VARCHAR(60))   BEGIN
    UPDATE tb_usuario
    SET
        dni = p_dni,
        nombres = p_nombres,
        apellidos = p_apellidos,
        correo = p_correo
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS `sp_agregar_curso`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_agregar_curso` (IN `p_id_matricula` INT, IN `p_id_curso` INT)   BEGIN
    INSERT INTO tb_detalle_matricula
    (id_matricula, id_curso)
    VALUES
    (p_id_matricula, p_id_curso);
END$$

DROP PROCEDURE IF EXISTS `sp_anular_matricula`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_anular_matricula` (IN `p_id_matricula` INT)   BEGIN
    UPDATE tb_matricula
    SET estado = 0
    WHERE id_matricula = p_id_matricula
    AND estado = 1;
END$$

DROP PROCEDURE IF EXISTS `sp_buscar_curso_id`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_buscar_curso_id` (IN `p_id_curso` INT)   BEGIN
    SELECT
        cu.id_curso,
        cu.nombre,
        cu.creditos,
        cu.horas,
        ca.id_carrera,
        ca.nombre AS carrera
    FROM tb_curso AS cu
    INNER JOIN tb_carrera AS ca
        ON cu.id_carrera = ca.id_carrera
    WHERE cu.id_curso = p_id_curso;
END$$

DROP PROCEDURE IF EXISTS `sp_buscar_curso_nombre`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_buscar_curso_nombre` (IN `p_nombre` VARCHAR(60))   BEGIN
    SELECT
        cu.id_curso,
        cu.nombre,
        cu.creditos,
        cu.horas,
        ca.id_carrera,
        ca.nombre AS carrera
    FROM tb_curso AS cu
    INNER JOIN tb_carrera AS ca
        ON cu.id_carrera = ca.id_carrera
    WHERE cu.nombre LIKE CONCAT(p_nombre, '%');
END$$

DROP PROCEDURE IF EXISTS `sp_buscar_pago_id`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_buscar_pago_id` (IN `p_id_pago` INT)   BEGIN
    SELECT
        id_pago,
        monto,
        fecha,
        metodo,
        id_matricula
    FROM tb_pago
    WHERE id_pago = p_id_pago;
END$$

DROP PROCEDURE IF EXISTS `sp_buscar_usuario_id`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_buscar_usuario_id` (IN `p_id_usuario` INT)   BEGIN
    SELECT
        id_usuario,
        dni,
        nombres,
        apellidos,
        correo
    FROM tb_usuario
    WHERE id_usuario = p_id_usuario;
END$$

DROP PROCEDURE IF EXISTS `sp_crear_curso`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_crear_curso` (IN `p_nombre` VARCHAR(60), IN `p_creditos` INT, IN `p_horas` INT, IN `p_id_carrera` INT)   BEGIN
    INSERT INTO tb_curso
    (nombre, creditos, horas, id_carrera)
    VALUES
    (p_nombre, p_creditos, p_horas, p_id_carrera);
END$$

DROP PROCEDURE IF EXISTS `sp_crear_matricula`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_crear_matricula` (IN `p_fecha` DATE, IN `p_id_estudiante` INT, IN `p_id_bloque` INT)   BEGIN
    INSERT INTO tb_matricula
    (fecha, id_estudiante, id_bloque)
    VALUES
    (p_fecha, p_id_estudiante, p_id_bloque);
END$$

DROP PROCEDURE IF EXISTS `sp_crear_pago`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_crear_pago` (IN `p_monto` DECIMAL(10,2), IN `p_fecha` DATE, IN `p_metodo` VARCHAR(30), IN `p_id_matricula` INT)   BEGIN
    INSERT INTO tb_pago (
        monto,
        fecha,
        metodo,
        id_matricula
    )
    VALUES (
        p_monto,
        p_fecha,
        p_metodo,
        p_id_matricula
    );
END$$

DROP PROCEDURE IF EXISTS `sp_crear_usuario`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_crear_usuario` (IN `p_dni` CHAR(8), IN `p_nombres` VARCHAR(50), IN `p_apellidos` VARCHAR(50), IN `p_correo` VARCHAR(60))   BEGIN
    INSERT INTO tb_usuario
    (dni, nombres, apellidos, correo)
    VALUES
    (p_dni, p_nombres, p_apellidos, p_correo);
END$$

DROP PROCEDURE IF EXISTS `sp_eliminar_matricula`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminar_matricula` (IN `p_id_matricula` INT)   BEGIN
    DELETE FROM tb_matricula
    WHERE id_matricula = p_id_matricula;
END$$

DROP PROCEDURE IF EXISTS `sp_eliminar_pago`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminar_pago` (IN `p_id_pago` INT)   BEGIN
    DELETE FROM tb_pago
    WHERE id_pago = p_id_pago;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_carreras`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_carreras` ()   BEGIN
SELECT ca.id_carrera, ca.nombre, ca.duracion_ciclos, COUNT(cu.id_curso) AS cantidad_cursos FROM tb_carrera AS ca
LEFT JOIN tb_curso AS cu ON ca.id_carrera = cu.id_carrera
GROUP BY ca.id_carrera, ca.nombre, ca.duracion_ciclos;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_cursos`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_cursos` ()   BEGIN
SELECT cu.id_curso, cu.nombre, cu.creditos, cu.horas, ca.nombre AS carrera FROM tb_curso AS cu
INNER JOIN tb_carrera AS ca ON cu.id_carrera = ca.id_carrera;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_detalle_matricula`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_detalle_matricula` (IN `p_id_matricula` INT)   BEGIN
SELECT d.id_matricula, cu.nombre AS curso, cu.creditos, d.nota FROM tb_detalle_matricula AS d
INNER JOIN tb_curso AS cu ON d.id_curso = cu.id_curso
WHERE d.id_matricula = p_id_matricula AND d.estado = 1;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_docentes`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_docentes` ()   BEGIN
SELECT d.id_usuario, u.nombres, u.apellidos, d.especialidad, d.grado_academico FROM tb_docente AS d
INNER JOIN tb_usuario AS u ON d.id_usuario = u.id_usuario;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_estudiantes`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_estudiantes` ()   BEGIN
SELECT e.id_usuario, e.codigo, u.nombres, u.apellidos, c.nombre AS carrera, e.fecha_ingreso FROM tb_estudiante AS e
INNER JOIN tb_usuario AS u ON e.id_usuario = u.id_usuario
INNER JOIN tb_carrera AS c ON e.id_carrera = c.id_carrera;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_horarios`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_horarios` ()   BEGIN
SELECT h.id_horario, h.dia, h.hora_inicio, h.hora_fin, cu.nombre AS curso, u.nombres AS docente, a.nombre AS aula FROM tb_horario AS h
INNER JOIN tb_curso AS cu ON h.id_curso = cu.id_curso
INNER JOIN tb_usuario AS u ON h.id_docente = u.id_usuario
INNER JOIN tb_aula AS a ON h.id_aula = a.id_aula;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_matriculas`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_matriculas` ()   BEGIN
SELECT m.id_matricula, m.fecha, u.nombres, u.apellidos, b.nombre AS bloque, COUNT(d.id_curso) AS cantidad_cursos FROM tb_matricula AS m
INNER JOIN tb_usuario AS u ON m.id_estudiante = u.id_usuario
INNER JOIN tb_bloque AS b ON m.id_bloque = b.id_bloque
LEFT JOIN tb_detalle_matricula AS d ON m.id_matricula = d.id_matricula AND d.estado = 1
WHERE m.estado = 1
GROUP BY m.id_matricula, m.fecha, u.nombres, u.apellidos, b.nombre;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_pagos`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_pagos` ()   BEGIN
SELECT p.id_pago, p.fecha, p.monto, p.metodo, p.id_matricula, u.nombres, u.apellidos FROM tb_pago AS p
INNER JOIN tb_matricula AS m ON p.id_matricula = m.id_matricula
INNER JOIN tb_usuario AS u ON m.id_estudiante = u.id_usuario;
END$$

DROP PROCEDURE IF EXISTS `sp_mostrar_usuarios`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mostrar_usuarios` ()   BEGIN
SELECT id_usuario, dni, nombres, apellidos, correo
FROM tb_usuario;
END$$

DROP PROCEDURE IF EXISTS `sp_retirar_curso`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_retirar_curso` (IN `p_id_matricula` INT, IN `p_id_curso` INT)   BEGIN
    UPDATE tb_detalle_matricula
    SET estado = 0
    WHERE id_matricula = p_id_matricula
    AND id_curso = p_id_curso
    AND estado = 1;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_aula`
--

DROP TABLE IF EXISTS `tb_aula`;
CREATE TABLE `tb_aula` (
  `id_aula` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `capacidad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_aula`
--

INSERT INTO `tb_aula` (`id_aula`, `nombre`, `capacidad`) VALUES
(1, 'Aula 101', 30),
(2, 'Aula 102', 30),
(3, 'Laboratorio 1', 25),
(4, 'Laboratorio 2', 25),
(5, 'Taller Electronica', 20),
(6, 'Taller Mecanico', 20);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_bloque`
--

DROP TABLE IF EXISTS `tb_bloque`;
CREATE TABLE `tb_bloque` (
  `id_bloque` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_bloque`
--

INSERT INTO `tb_bloque` (`id_bloque`, `nombre`, `fecha_inicio`, `fecha_fin`) VALUES
(1, 'Bloque 1 - 2026', '2026-01-12', '2026-03-27'),
(2, 'Bloque 2 - 2026', '2026-04-06', '2026-06-19'),
(3, 'Bloque 3 - 2026', '2026-07-06', '2026-09-18'),
(4, 'Bloque 4 - 2026', '2026-10-05', '2026-12-18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_carrera`
--

DROP TABLE IF EXISTS `tb_carrera`;
CREATE TABLE `tb_carrera` (
  `id_carrera` int(11) NOT NULL,
  `nombre` varchar(60) NOT NULL,
  `duracion_ciclos` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_carrera`
--

INSERT INTO `tb_carrera` (`id_carrera`, `nombre`, `duracion_ciclos`) VALUES
(1, 'Desarrollo de Software', 6),
(2, 'Administracion Industrial', 6),
(3, 'Electronica y Automatizacion', 6),
(4, 'Diseno Grafico Digital', 4),
(5, 'Soldadura Industrial', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_curso`
--

DROP TABLE IF EXISTS `tb_curso`;
CREATE TABLE `tb_curso` (
  `id_curso` int(11) NOT NULL,
  `nombre` varchar(60) NOT NULL,
  `creditos` int(11) NOT NULL,
  `horas` int(11) NOT NULL,
  `id_carrera` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_curso`
--

INSERT INTO `tb_curso` (`id_curso`, `nombre`, `creditos`, `horas`, `id_carrera`) VALUES
(1, 'Programacion Orientada a Objetos', 4, 64, 1),
(2, 'Base de Datos', 4, 64, 1),
(3, 'Desarrollo Web', 4, 64, 1),
(4, 'Analisis y Diseno de Sistemas', 3, 48, 1),
(5, 'Gestion de Proyectos', 3, 48, 2),
(6, 'Contabilidad Basica', 3, 48, 2),
(7, 'Circuitos Electricos', 4, 64, 3),
(8, 'Automatizacion Industrial', 4, 64, 3),
(9, 'Diseno Digital', 3, 48, 4),
(10, 'Soldadura Basica', 3, 48, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_detalle_matricula`
--

DROP TABLE IF EXISTS `tb_detalle_matricula`;
CREATE TABLE `tb_detalle_matricula` (
  `id_matricula` int(11) NOT NULL,
  `id_curso` int(11) NOT NULL,
  `nota` decimal(4,2) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ;

--
-- Volcado de datos para la tabla `tb_detalle_matricula`
--

INSERT INTO `tb_detalle_matricula` (`id_matricula`, `id_curso`, `nota`, `estado`) VALUES
(1, 1, NULL, 1),
(1, 2, NULL, 1),
(2, 1, NULL, 1),
(2, 3, NULL, 1),
(3, 5, NULL, 1),
(3, 6, NULL, 1),
(4, 7, NULL, 1),
(4, 8, NULL, 1),
(5, 1, NULL, 1),
(5, 4, NULL, 1),
(6, 9, NULL, 1),
(7, 3, 15.50, 1),
(7, 4, 17.00, 1),
(8, 2, 14.00, 1),
(8, 3, 16.50, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_docente`
--

DROP TABLE IF EXISTS `tb_docente`;
CREATE TABLE `tb_docente` (
  `id_usuario` int(11) NOT NULL,
  `especialidad` varchar(50) NOT NULL,
  `grado_academico` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_docente`
--

INSERT INTO `tb_docente` (`id_usuario`, `especialidad`, `grado_academico`) VALUES
(1, 'Programacion', 'Ingeniero'),
(2, 'Base de datos', 'Magister'),
(3, 'Administracion', 'Licenciado'),
(4, 'Electronica', 'Ingeniero');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_estudiante`
--

DROP TABLE IF EXISTS `tb_estudiante`;
CREATE TABLE `tb_estudiante` (
  `id_usuario` int(11) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `fecha_ingreso` date NOT NULL,
  `id_carrera` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_estudiante`
--

INSERT INTO `tb_estudiante` (`id_usuario`, `codigo`, `fecha_ingreso`, `id_carrera`) VALUES
(5, 'E2026001', '2026-01-12', 1),
(6, 'E2026002', '2026-01-12', 1),
(7, 'E2026003', '2026-01-12', 2),
(8, 'E2026004', '2026-04-06', 3),
(9, 'E2026005', '2026-04-06', 1),
(10, 'E2026006', '2026-07-06', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_horario`
--

DROP TABLE IF EXISTS `tb_horario`;
CREATE TABLE `tb_horario` (
  `id_horario` int(11) NOT NULL,
  `dia` varchar(10) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `id_curso` int(11) NOT NULL,
  `id_docente` int(11) NOT NULL,
  `id_aula` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_horario`
--

INSERT INTO `tb_horario` (`id_horario`, `dia`, `hora_inicio`, `hora_fin`, `id_curso`, `id_docente`, `id_aula`) VALUES
(1, 'Viernes', '08:00:00', '12:00:00', 1, 1, 3),
(2, 'Sabado', '08:00:00', '12:00:00', 2, 2, 4),
(3, 'Viernes', '13:00:00', '17:00:00', 3, 1, 3),
(4, 'Sabado', '13:00:00', '17:00:00', 4, 2, 1),
(5, 'Lunes', '08:00:00', '11:00:00', 5, 3, 2),
(6, 'Martes', '08:00:00', '11:00:00', 6, 3, 2),
(7, 'Miercoles', '14:00:00', '18:00:00', 7, 4, 5),
(8, 'Jueves', '14:00:00', '18:00:00', 8, 4, 5),
(9, 'Lunes', '14:00:00', '17:00:00', 9, 3, 1),
(10, 'Martes', '14:00:00', '17:00:00', 10, 4, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_matricula`
--

DROP TABLE IF EXISTS `tb_matricula`;
CREATE TABLE `tb_matricula` (
  `id_matricula` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1,
  `id_estudiante` int(11) NOT NULL,
  `id_bloque` int(11) NOT NULL
) ;

--
-- Volcado de datos para la tabla `tb_matricula`
--

INSERT INTO `tb_matricula` (`id_matricula`, `fecha`, `estado`, `id_estudiante`, `id_bloque`) VALUES
(1, '2026-10-01', 1, 5, 4),
(2, '2026-10-01', 1, 6, 4),
(3, '2026-10-02', 1, 7, 4),
(4, '2026-10-02', 1, 8, 4),
(5, '2026-10-03', 1, 9, 4),
(6, '2026-10-03', 1, 10, 4),
(7, '2026-07-01', 1, 5, 3),
(8, '2026-07-01', 1, 6, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_pago`
--

DROP TABLE IF EXISTS `tb_pago`;
CREATE TABLE `tb_pago` (
  `id_pago` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha` date NOT NULL,
  `metodo` varchar(30) NOT NULL,
  `id_matricula` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_pago`
--

INSERT INTO `tb_pago` (`id_pago`, `monto`, `fecha`, `metodo`, `id_matricula`) VALUES
(1, 350.00, '2026-10-01', 'Efectivo', 1),
(2, 350.00, '2026-10-01', 'Yape', 2),
(3, 300.00, '2026-10-02', 'Tarjeta', 3),
(4, 300.00, '2026-10-02', 'Transferencia', 4),
(5, 350.00, '2026-10-03', 'Plin', 5),
(6, 280.00, '2026-10-03', 'Efectivo', 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tb_usuario`
--

DROP TABLE IF EXISTS `tb_usuario`;
CREATE TABLE `tb_usuario` (
  `id_usuario` int(11) NOT NULL,
  `dni` char(8) NOT NULL,
  `nombres` varchar(50) NOT NULL,
  `apellidos` varchar(50) NOT NULL,
  `correo` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tb_usuario`
--

INSERT INTO `tb_usuario` (`id_usuario`, `dni`, `nombres`, `apellidos`, `correo`) VALUES
(1, '40123456', 'Carlos', 'Mendoza Rojas', 'cmendoza@senati.pe'),
(2, '41234567', 'Lucia', 'Fernandez Silva', 'lfernandez@senati.pe'),
(3, '42345678', 'Jorge', 'Paredes Luna', 'jparedes@senati.pe'),
(4, '43456789', 'Rosa', 'Vega Castillo', 'rvega@senati.pe'),
(5, '70123456', 'Diego', 'Ramirez Torres', 'diego.ramirez@gmail.com'),
(6, '71234567', 'Andrea', 'Flores Huaman', 'andrea.flores@gmail.com'),
(7, '72345678', 'Luis', 'Gonzales Ccori', 'luis.gonzales@gmail.com'),
(8, '73456789', 'Sofia', 'Herrera Salas', 'sofia.herrera@gmail.com'),
(9, '74567890', 'Miguel', 'Vargas Ponce', 'miguel.vargas@gmail.com'),
(10, '75678901', 'Camila', 'Rojas Medina', 'camila.rojas@gmail.com');

--
-- Índices para tablas volcadas
--
  
--

-- Indices de la tabla `tb_aula`
--
ALTER TABLE `tb_aula`
  ADD PRIMARY KEY (`id_aula`);

--
-- Indices de la tabla `tb_bloque`
--
ALTER TABLE `tb_bloque`
  ADD PRIMARY KEY (`id_bloque`);

--
-- Indices de la tabla `tb_carrera`
--
ALTER TABLE `tb_carrera`
  ADD PRIMARY KEY (`id_carrera`);

--
-- Indices de la tabla `tb_curso`
--
ALTER TABLE `tb_curso`
  ADD PRIMARY KEY (`id_curso`),
  ADD KEY `id_carrera` (`id_carrera`);

--
-- Indices de la tabla `tb_detalle_matricula`
--
ALTER TABLE `tb_detalle_matricula`
  ADD PRIMARY KEY (`id_matricula`,`id_curso`),
  ADD KEY `id_curso` (`id_curso`);

--
-- Indices de la tabla `tb_docente`
--
ALTER TABLE `tb_docente`
  ADD PRIMARY KEY (`id_usuario`);

--
-- Indices de la tabla `tb_estudiante`
--
ALTER TABLE `tb_estudiante`
  ADD PRIMARY KEY (`id_usuario`),
  ADD KEY `id_carrera` (`id_carrera`);

--
-- Indices de la tabla `tb_horario`
--
ALTER TABLE `tb_horario`
  ADD PRIMARY KEY (`id_horario`),
  ADD KEY `id_curso` (`id_curso`),
  ADD KEY `id_docente` (`id_docente`),
  ADD KEY `id_aula` (`id_aula`);

--
-- Indices de la tabla `tb_matricula`
--
ALTER TABLE `tb_matricula`
  ADD PRIMARY KEY (`id_matricula`),
  ADD KEY `id_estudiante` (`id_estudiante`),
  ADD KEY `id_bloque` (`id_bloque`);

--
-- Indices de la tabla `tb_pago`
--
ALTER TABLE `tb_pago`
  ADD PRIMARY KEY (`id_pago`),
  ADD KEY `id_matricula` (`id_matricula`);

--
-- Indices de la tabla `tb_usuario`
--
ALTER TABLE `tb_usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `dni` (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `tb_aula`
--
ALTER TABLE `tb_aula`
  MODIFY `id_aula` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `tb_bloque`
--
ALTER TABLE `tb_bloque`
  MODIFY `id_bloque` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `tb_carrera`
--
ALTER TABLE `tb_carrera`
  MODIFY `id_carrera` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `tb_curso`
--
ALTER TABLE `tb_curso`
  MODIFY `id_curso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `tb_horario`
--
ALTER TABLE `tb_horario`
  MODIFY `id_horario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `tb_matricula`
--
ALTER TABLE `tb_matricula`
  MODIFY `id_matricula` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tb_pago`
--
ALTER TABLE `tb_pago`
  MODIFY `id_pago` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `tb_usuario`
--
ALTER TABLE `tb_usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `tb_curso`
--
ALTER TABLE `tb_curso`
  ADD CONSTRAINT `tb_curso_ibfk_1` FOREIGN KEY (`id_carrera`) REFERENCES `tb_carrera` (`id_carrera`);

--
-- Filtros para la tabla `tb_detalle_matricula`
--
ALTER TABLE `tb_detalle_matricula`
  ADD CONSTRAINT `tb_detalle_matricula_ibfk_1` FOREIGN KEY (`id_matricula`) REFERENCES `tb_matricula` (`id_matricula`) ON DELETE CASCADE,
  ADD CONSTRAINT `tb_detalle_matricula_ibfk_2` FOREIGN KEY (`id_curso`) REFERENCES `tb_curso` (`id_curso`);

--
-- Filtros para la tabla `tb_docente`
--
ALTER TABLE `tb_docente`
  ADD CONSTRAINT `tb_docente_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `tb_usuario` (`id_usuario`);

--
-- Filtros para la tabla `tb_estudiante`
--
ALTER TABLE `tb_estudiante`
  ADD CONSTRAINT `tb_estudiante_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `tb_usuario` (`id_usuario`),
  ADD CONSTRAINT `tb_estudiante_ibfk_2` FOREIGN KEY (`id_carrera`) REFERENCES `tb_carrera` (`id_carrera`);

--
-- Filtros para la tabla `tb_horario`
--
ALTER TABLE `tb_horario`
  ADD CONSTRAINT `tb_horario_ibfk_1` FOREIGN KEY (`id_curso`) REFERENCES `tb_curso` (`id_curso`),
  ADD CONSTRAINT `tb_horario_ibfk_2` FOREIGN KEY (`id_docente`) REFERENCES `tb_docente` (`id_usuario`),
  ADD CONSTRAINT `tb_horario_ibfk_3` FOREIGN KEY (`id_aula`) REFERENCES `tb_aula` (`id_aula`);

--
-- Filtros para la tabla `tb_matricula`
--
ALTER TABLE `tb_matricula`
  ADD CONSTRAINT `tb_matricula_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `tb_estudiante` (`id_usuario`),
  ADD CONSTRAINT `tb_matricula_ibfk_2` FOREIGN KEY (`id_bloque`) REFERENCES `tb_bloque` (`id_bloque`);

--
-- Filtros para la tabla `tb_pago`
--
ALTER TABLE `tb_pago`
  ADD CONSTRAINT `tb_pago_ibfk_1` FOREIGN KEY (`id_matricula`) REFERENCES `tb_matricula` (`id_matricula`);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

CREATE TABLE tb_credencial (
    id_usuario INT(11) NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_usuario),
    CONSTRAINT fk_credencial_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES tb_usuario(id_usuario)
        ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci;

  
INSERT INTO tb_credencial (id_usuario, password_hash, rol)
VALUES (
    1,
    '$2a$10$m444qLLqTrlwkK4Ac4qL5OdYOwcQ2CaIrW4JyvrWKGoO2HoGTRY0e',
    'DOCENTE'
);  
  


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
