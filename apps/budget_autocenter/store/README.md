# store/ — dados do AutoCenter na Google Play

Fonte única do que vai para o Play Console. Os agentes leem daqui; ninguém
digita no Console à mão.

| Arquivo | Conteúdo | Usado por |
|---|---|---|
| `ficha.yaml` | Textos da loja, categoria, contato, conteúdo do app (política, anúncios, acesso, classificação, público), lançamento | `criar-app-play` (navegador) e `publicar-play` (API) |
| `data-safety.yaml` | Respostas de Segurança dos dados, com a justificativa técnica de cada uma | `criar-app-play` |
| `graficos/` | Ícone, imagem de destaque e capturas de tela | os dois agentes |

**Sigilo:** nenhum login, senha ou token entra aqui. Credenciais do revisor do
Google ficam no cofre (`D:/Vault/.segredos/android/budget_autocenter/`), e a
ficha só aponta para o arquivo.

**Para um app novo:** copie esta pasta para `apps/<app>/store/` e ajuste.

## Gráficos exigidos

| Arquivo | Formato | Situação |
|---|---|---|
| `graficos/icone-512.png` | 512×512, PNG 32 bits, até 1 MB | pronto (o `ic_launcher` do app usa a mesma arte, em círculo) |
| `graficos/destaque-1024x500.png` | 1024×500, PNG ou JPG sem transparência | pronto |
| `graficos/telefone/*.png` | 2 a 8 capturas, proporção 9:16, lado entre 320 e 3840 px | **falta** |
| `graficos/tablet-7/`, `graficos/tablet-10/` | opcionais | — |

As artes saem de `graficos/fonte/*.svg` (fonte editável). Para regerar os PNG, renderize os SVG no Edge sem
interface (`msedge --headless=new --screenshot`) nos tamanhos acima; ícone da loja precisa ser RGBA (32 bits),
destaque sem transparência, e o `launcher.svg` vai para `android/app/src/main/res/mipmap-*/ic_launcher.png`
(48, 72, 96, 144 e 192 px).

## Pendências do rascunho (busque `REVISAR`)

1. Título na loja e categoria.
2. E-mail de contato público e site.
3. URL da política de privacidade.
4. Servidor de demonstração + usuário para o revisor (só para fechado/aberto/produção).
5. Lista de testadores internos.
6. Confirmar as respostas de Segurança dos dados.
