#!/bin/bash

# Script de build multiplataforma para RMI Museum
# Baseado no POM.xml configurado com profiles

set -e

APP_NAME="RMI-Museum"
echo "🏛️ RMI Museum - Build Multiplataforma"
echo "======================================"

# Função para detectar OS
detect_os() {
    case "$(uname -s)" in
        Darwin*)    echo "macos" ;;
        Linux*)     echo "linux" ;;
        CYGWIN*|MINGW*|MSYS*) echo "windows" ;;
        *)          echo "unknown" ;;
    esac
}

CURRENT_OS=$(detect_os)

# Função para mostrar help
show_help() {
    echo "Uso: $0 [OPÇÃO]"
    echo ""
    echo "Opções:"
    echo "  dev        - Executar em modo desenvolvimento"
    echo "  compile    - Apenas compilar o projeto"
    echo "  jar        - Criar JAR normal"
    echo "  fat-jar    - Criar Fat JAR (Assembly)"
    echo "  universal  - Criar pacote universal (Shade + launchers)"
    echo "  native     - Criar executável nativo para SO atual"
    echo "  all        - Criar todos os pacotes para SO atual"
    echo "  clean      - Limpar builds anteriores"
    echo ""
    echo "Builds específicos por SO:"
    echo "  macos      - Criar .dmg e .app para macOS"
    echo "  windows    - Criar .exe e .msi para Windows"
    echo "  linux      - Criar .deb, .rpm e AppImage para Linux"
    echo ""
    echo "Sistema atual detectado: $CURRENT_OS"
}

# Função para limpar builds
clean_builds() {
    echo "🧹 Limpando builds anteriores..."
    mvn clean
    rm -rf target/dist-*
    rm -rf target/universal-dist
    echo "✅ Limpeza concluída"
}

# Função para compilar
compile_project() {
    echo "🔨 Compilando projeto..."
    mvn clean compile
    echo "✅ Compilação concluída"
}

# Função para executar em desenvolvimento
run_dev() {
    echo "🚀 Executando em modo desenvolvimento..."
    mvn clean javafx:run
}

# Função para criar JAR normal
create_jar() {
    echo "📦 Criando JAR normal..."
    mvn clean package -DskipTests
    echo "✅ JAR criado: target/RMI_Museum-1.0-SNAPSHOT.jar"
}

# Função para criar Fat JAR
create_fat_jar() {
    echo "📦 Criando Fat JAR..."
    mvn clean package -DskipTests
    if [ -f "target/RMI-Museum-universal.jar" ]; then
        echo "✅ Fat JAR criado: target/RMI-Museum-universal.jar"
    else
        echo "❌ Erro ao criar Fat JAR"
        return 1
    fi
}

# Função para criar pacote universal
create_universal() {
    echo "🌍 Criando pacote universal..."
    mvn clean package -Puniversal -DskipTests
    
    if [ -d "target/universal-dist" ]; then
        echo "✅ Pacote universal criado em: target/universal-dist/"
        echo "📁 Conteúdo:"
        ls -la target/universal-dist/
        echo ""
        echo "💡 Para executar:"
        echo "  Linux/macOS: ./target/universal-dist/run.sh"
        echo "  Windows:     target/universal-dist/run.bat"
    else
        echo "❌ Erro ao criar pacote universal"
        return 1
    fi
}

# Função para criar executável nativo
create_native() {
    echo "🏠 Criando executável nativo para $CURRENT_OS..."
    
    case $CURRENT_OS in
        "macos")
            mvn clean package install -Pmacos -DskipTests
            ;;
        "linux")
            mvn clean package install -Plinux -DskipTests
            ;;
        "windows")
            mvn clean package install -Pwindows -DskipTests
            ;;
        *)
            echo "❌ SO não suportado para build nativo: $CURRENT_OS"
            return 1
            ;;
    esac
    
    echo "✅ Build nativo concluído para: $CURRENT_OS"
    show_build_results
}

# Função para build específico por SO
build_for_os() {
    local target_os=$1
    
    echo "🎯 Criando build para: $target_os"
    
    case $target_os in
        "macos")
            if [ "$CURRENT_OS" = "macos" ]; then
                mvn clean package install -Pmacos -DskipTests
                echo "✅ Build macOS concluído:"
                find target/dist-macos* -name "*.dmg" -o -name "*.app" 2>/dev/null | head -5
            else
                echo "⚠️  Build para macOS só pode ser executado em macOS"
                echo "💡 Use GitHub Actions ou acesso remoto a um Mac"
            fi
            ;;
        "windows")
            if [ "$CURRENT_OS" = "windows" ]; then
                mvn clean package install -Pwindows -DskipTests
                echo "✅ Build Windows concluído:"
                find target/dist-windows* -name "*.exe" -o -name "*.msi" 2>/dev/null | head -5
            else
                echo "⚠️  Build para Windows só pode ser executado em Windows"
                echo "💡 Use GitHub Actions ou máquina virtual Windows"
            fi
            ;;
        "linux")
            if [ "$CURRENT_OS" = "linux" ]; then
                mvn clean package install -Plinux -DskipTests
                echo "✅ Build Linux concluído:"
                find target/dist-linux* -name "*.deb" -o -name "*.rpm" 2>/dev/null | head -5
            else
                echo "⚠️  Build para Linux só pode ser executado em Linux"
                echo "💡 Use GitHub Actions ou Docker"
            fi
            ;;
        *)
            echo "❌ SO não reconhecido: $target_os"
            return 1
            ;;
    esac
}

# Função para mostrar resultados
show_build_results() {
    echo ""
    echo "📊 Resultados do Build:"
    echo "======================="
    
    # JAR normal
    if [ -f "target/RMI_Museum-1.0-SNAPSHOT.jar" ]; then
        echo "📦 JAR: target/RMI_Museum-1.0-SNAPSHOT.jar ($(du -h target/RMI_Museum-1.0-SNAPSHOT.jar | cut -f1))"
    fi
    
    # Fat JAR
    if [ -f "target/RMI-Museum-universal.jar" ]; then
        echo "📦 Fat JAR: target/RMI-Museum-universal.jar ($(du -h target/RMI-Museum-universal.jar | cut -f1))"
    fi
    
    # Universal dist
    if [ -d "target/universal-dist" ]; then
        echo "🌍 Universal: target/universal-dist/ ($(du -sh target/universal-dist | cut -f1))"
    fi
    
    # Executáveis nativos
    for dist_dir in target/dist-*; do
        if [ -d "$dist_dir" ]; then
            platform=$(basename "$dist_dir" | sed 's/dist-//' | sed 's/-.*$//')
            echo "🎯 $platform: $dist_dir/ ($(du -sh "$dist_dir" | cut -f1))"
        fi
    done
}

# Função principal
create_all() {
    echo "🎯 Criando todos os pacotes para $CURRENT_OS..."
    
    create_jar
    create_fat_jar
    create_universal
    create_native
    
    show_build_results
}

# Processar argumentos
case "${1:-help}" in
    "dev")
        run_dev
        ;;
    "compile")
        compile_project
        ;;
    "jar")
        create_jar
        ;;
    "fat-jar")
        create_fat_jar
        ;;
    "universal")
        create_universal
        ;;
    "native")
        create_native
        ;;
    "all")
        create_all
        ;;
    "clean")
        clean_builds
        ;;
    "macos")
        build_for_os "macos"
        ;;
    "windows")
        build_for_os "windows"
        ;;
    "linux")
        build_for_os "linux"
        ;;
    "help"|"--help"|"-h")
        show_help
        ;;
    *)
        echo "❌ Opção inválida: $1"
        echo ""
        show_help
        exit 1
        ;;
esac