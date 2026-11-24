# App Câmera (Java Android)

> Repositório didático: exercício prático de Android nativo com **Java**, desenvolvido e aplicado em sala de aula pelo Prof. Fabricio Curvello.

## Sobre o projeto

Aplicativo que abre a câmera do aparelho ao tocar na imagem da tela, tira uma foto e a exibe no `ImageView`, corrigindo a rotação da imagem.

## Contexto acadêmico

Exercício aplicado em sala de aula, usado como prática guiada com os alunos.

## O que o app faz

- Ao tocar na imagem, verifica a permissão de câmera e a solicita, se necessário.
- Abre o app de câmera com `MediaStore.ACTION_IMAGE_CAPTURE`.
- Salva a foto em arquivo usando `FileProvider`.
- Lê os metadados EXIF da foto e corrige a orientação antes de exibi-la.

## Conceitos trabalhados em aula

- Permissões em tempo de execução (`CAMERA`)
- Intents implícitas para usar a câmera do sistema
- `ActivityResultLauncher` (Activity Result API)
- `FileProvider` para compartilhar arquivos com segurança
- Manipulação de `Bitmap` e leitura de EXIF (`ExifInterface`)

## Tecnologias

- **Java**
- **Android SDK** (Android Studio)
- **Gradle**
- AndroidX Activity Result API
- `FileProvider` / `ExifInterface`
- `minSdk 28` / `compileSdk 34`

## Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/fabcurvello/AppCameraJavaAndroid.git
   ```
2. Abra a pasta no **Android Studio** e aguarde a sincronização do Gradle.
3. Execute em um emulador ou dispositivo físico com Android compatível com a API 28 ou superior.

## Veja também

Outros repositórios de aula com o mesmo tema:

- [AppCameraAnima](https://github.com/fabcurvello/AppCameraAnima)

## Autor

**Fabricio Curvello**: professor de desenvolvimento de software no SENAI-RJ (desde 1997) e em faculdades do grupo Ânima Educação (IBMR e UNA).

- GitHub: [github.com/fabcurvello](https://github.com/fabcurvello)
- LinkedIn: [linkedin.com/in/fabriciocurvello](https://www.linkedin.com/in/fabriciocurvello)
- YouTube: [@fabriciocurvello](https://www.youtube.com/@fabriciocurvello)
