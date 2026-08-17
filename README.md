# SimpleWhitelist

O **SimpleWhitelist** é um plugin de gerenciamento de whitelist para servidores de Minecraft (Paper/Spigot) focado em **servidores piratas (offline-mode)**.

Diferente da whitelist nativa do Minecraft que valida o acesso do jogador via UUID, o **SimpleWhitelist** ignora o UUID e realiza a verificação de acesso estritamente pelo **nick (username)** do jogador.

---

## ⚙️ Pré-requisitos & Configuração

Para garantir que o plugin funcione sem interrupções ou conflitos:

1. **Desative a whitelist nativa:** No arquivo `server.properties`, altere a opção para `false`:
```properties
white-list=false

```


2. **Dependências obrigatórias:** Certifique-se de ter o plugin [LuckPerms](https://luckperms.net/) instalado na pasta `plugins/` do servidor.

---

## 📜 Comandos

Todos os comandos utilizam o alias principal `/swl`:

| Comando | Descrição |
| --- | --- |
| `/swl add <nick>` | Adiciona o nick informado à whitelist |
| `/swl rem <nick>` | Remove o nick informado da whitelist |
| `/swl list` | Lista todos os nicks cadastrados na whitelist |
| `/swl reload` | Recarrega as configurações do plugin |

---

## 🔑 Permissões

Configure as permissões dos seus administradores ou cargos via LuckPerms:

| Permissão | Descrição |
| --- | --- |
| `simplewhitelist.admin` | Concede acesso total a todos os comandos do plugin |
| `simplewhitelist.use.add` | Permite usar `/swl add` |
| `simplewhitelist.use.rem` | Permite usar `/swl rem` |
| `simplewhitelist.use.list` | Permite usar `/swl list` |
| `simplewhitelist.use.reload` | Permite usar `/swl reload` |

---

## 💻 API para Desenvolvedores

O **SimpleWhitelist** disponibiliza uma API interna para integração local entre plugins na mesma instância, permitindo consultar e manipular a whitelist sem a necessidade de requisições externas ou conexões HTTP/banco de dados adicionais.

### Dependência Maven

Para utilizar a API em outro plugin, adicione o JAR ao seu classpath ou adicione como dependência local no `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github-solariaproject</id>
        <url>https://maven.pkg.github.com/SolariaProject/SimpleWhitelist</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>solaria.simplewhitelist</groupId>
        <artifactId>simplewhitelist</artifactId>
        <version>1.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

```

Adicione o `SimpleWhitelist` nas dependências do seu `paper-plugin.yml` ou `plugin.yml`:

```yaml
depend: [ SimpleWhitelist ]

```

### Exemplo de Uso da API

``` java
import solaria.simplewhitelist.Whitelist
// Exemplo de acesso à instância/gerenciador da API
Whitelist whitelist = Whitelist.getWhitelist();
boolean isWhitelisted = whitelist.hasPlayer("NickDoJogador");

if (!isWhitelisted) {
    whitelist.add("NickDoJogador");
}

```

---

## 🛠️ Informações Técnicas

* **Group ID:** `solaria.simplewhitelist`
* **Artifact ID:** `simplewhitelist`
* **Versão:** `1.0`
* **Target API:** `26.1.2`
* **Load Stage:** `POSTWORLD`

---

## 📄 Licença

Este projeto é de código aberto e está disponível sob a licença [MIT](https://www.google.com/search?q=LICENSE).