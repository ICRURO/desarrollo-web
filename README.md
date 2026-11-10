# GameBuildArchive | Enciclopedia Interactiva y Build Planner

## Descripción

GameBuildArchive es una aplicación web que permite consultar información de videojuegos por parte de una comunidad de jugadores y crear tus propias configuraciones y guías.

Videojuegos principales que incluye GameBuildArchive en esta primera versión.

- Hearts of Iron IV
- Zenless Zone Zero
- Dying Light

Cada videojuego cuenta con una guía (prioritario) y un simulador de configuraciones (no prioritario) adaptado a sus características.

## Tecnologías

- **Frontend**: HTML, CSS y JavaScript
- **Backend**: Java y Spring Boot
- **Base de datos**: PostgreSQL
- **Comunicación**: API REST con Fetch
- **Entorno de desarrollo**: Ubuntu en WSL
- **Control de versiones**: Git y GitHub

## Estructura del proyecto

- **backend**: Código de Spring Boot.
- **frontend**: Archivos HTML, CSS y JavaScript.
- **database**: Scripts SQL para crear y llenar la base de datos.
- **documentacion**: Diseño y reportes.

## Instalación en otra computadora

Instrucciones para utilizar el repositorio en WSL y conectar la base de datos.

### 1. Instalar WSL

Abrir PowerShell y escribir el comando:

```bash
wsl --install
```

Ya instalado abrir WSL con el comando:

```bash
wsl
```

### 2. Instalar git

Estando en wsl se usa el comando:

```bash
sudo apt install git -y
```

### 3. Clonar el repositorio

Antes de clonar el repositorio de github se debe crear una carpeta.

```bash
mkdir -p ~/proyecto-final
cd ~/proyecto-final
```

Ya en la carpeta se puede clonar el repositorio.

```bash
git clone https://github.com/ICRURO/desarrollo-web.git
cd desarrollo-web
```
