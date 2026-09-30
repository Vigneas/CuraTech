# Setup Maven
Write-Host "Downloading Maven..."
Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip" -OutFile "maven.zip"
Write-Host "Extracting Maven..."
Expand-Archive -Path "maven.zip" -DestinationPath "maven_env" -Force
$env:Path += ";" + (Resolve-Path "maven_env\apache-maven-3.9.6\bin").Path

# Setup MariaDB
Write-Host "Downloading MariaDB (MySQL drop-in)..."
Invoke-WebRequest -Uri "https://archive.mariadb.org/mariadb-10.11.4/winx64-packages/mariadb-10.11.4-winx64.zip" -OutFile "mariadb.zip"
Write-Host "Extracting MariaDB..."
Expand-Archive -Path "mariadb.zip" -DestinationPath "mariadb_env" -Force

$mariaDbPath = (Resolve-Path "mariadb_env\mariadb-10.11.4-winx64").Path
Write-Host "Initializing Database..."
& "$mariaDbPath\bin\mysql_install_db.exe" --datadir="$mariaDbPath\data"

Write-Host "Setup Complete."
