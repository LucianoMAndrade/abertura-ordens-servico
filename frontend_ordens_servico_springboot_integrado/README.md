# Front-end integrado — Abertura de Ordens de Serviço

Este projeto React + Vite foi preparado com base nos controllers, DTOs e modelos encontrados no projeto Spring Boot enviado.

## Integração com o back-end

O back-end Spring Boot está configurado na porta `9091`. Durante o desenvolvimento, o Vite encaminha `/api/...` para `http://localhost:9091/...` e remove o prefixo `/api`.

Endpoints usados:
- `GET /ordem-servicos` — lista ordens.
- `GET /ordem-servicos/{id}` — consulta uma ordem.
- `POST /ordem-servicos` — abre ordem com `{ "equipamentoId": 1, "descricao": "..." }`.
- `GET /equipamentos` e `GET /equipamentos/{id}`.
- `POST /equipamentos` e `PUT /equipamentos/{id}` — corpo `{ "nome": "...", "numeroPatrimonio": "...", "setorId": 1 }`.
- `DELETE /equipamentos/{id}`.
- `GET /setor` e `GET /setor/{id}`.
- `POST /setor` e `PUT /setor/{id}` — corpo `{ "nome": "..." }`.
- `DELETE /setor/{id}`.

A propriedade de identificação da ordem é `ordemServicoId`, conforme o getter `getOrdemServicoId()` da classe `OrdemServico`. Os campos da ordem são `descricao`, `dataAbertura` e `equipamento`. Equipamentos retornam `id`, `nome`, `numeroPatrimonio` e `setor`; setores retornam `id` e `nome`.

## Executar localmente

1. Instale Node.js 18 ou superior.
2. Inicie o MySQL e execute o back-end Spring Boot na porta `9091`.
3. Abra um terminal nesta pasta e execute:

   ```bash
   npm install
   npm run dev
   ```

4. Abra `http://localhost:5173` (ou o endereço indicado pelo Vite).

Não é necessário configurar CORS para a execução local padrão, pois o navegador conversa com o servidor Vite e este encaminha as chamadas para o Spring Boot.

## Funcionalidades

- Painel de resumo com totais de ordens, equipamentos e setores.
- Listagem e busca de ordens de serviço.
- Abertura de ordem de serviço selecionando equipamento e informando descrição.
- Cadastro, edição e exclusão de equipamentos.
- Cadastro, edição e exclusão de setores.
- Tratamento de falhas da API e mensagens de retorno.
- Interface responsiva para desktop e celular.

## Limites conhecidos

- O back-end não possui endpoints de edição ou exclusão de ordens de serviço, portanto essas ações não aparecem na interface.
- Exclusões podem falhar se existirem relacionamentos no banco de dados que impeçam remover o registro.
- A integração foi montada a partir do código-fonte enviado; confirme a execução com seu ambiente Spring Boot/MySQL local.
- Se sua configuração local usar outro host ou porta, altere o `target` em `vite.config.js`.
