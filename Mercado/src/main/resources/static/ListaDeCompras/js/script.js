const API = "/api";

const configuracoes = {
    users: {
        endpoint: "usuarios", titulo: "Usuários", singular: "usuário", descricao: "Cadastre quem utiliza o sistema.",
        campos: [
            { id: "nome", label: "Nome", required: true },
            { id: "email", label: "E-mail", type: "email", required: true }
        ], colunas: ["ID", "Nome", "E-mail"]
    },
    categories: {
        endpoint: "categorias", titulo: "Categorias", singular: "categoria", descricao: "Organize os produtos por categoria.",
        campos: [
            { id: "nome", label: "Nome", required: true },
            { id: "descricao", label: "Descrição" }
        ], colunas: ["ID", "Nome", "Descrição"]
    },
    products: {
        endpoint: "produtos", titulo: "Produtos", singular: "produto", descricao: "Cadastre os produtos que poderão entrar nas listas.",
        campos: [
            { id: "nome", label: "Nome", required: true },
            { id: "descricao", label: "Descrição" },
            { id: "categoriaId", label: "Categoria", type: "select", source: "categories", relation: "categoria", optional: true }
        ], colunas: ["ID", "Produto", "Descrição", "Categoria"]
    },
    lists: {
        endpoint: "listas", titulo: "Listas de compras", singular: "lista", descricao: "Crie listas e associe-as a um usuário.",
        campos: [
            { id: "nome", label: "Nome da lista", required: true },
            { id: "usuarioId", label: "Responsável", type: "select", source: "users", relation: "usuario", required: true }
        ], colunas: ["ID", "Lista", "Responsável", "Criada em"]
    },
    items: {
        endpoint: "itens", titulo: "Itens das listas", singular: "item", descricao: "Adicione produtos às listas, informe quantidades e acompanhe o que já foi comprado.",
        campos: [
            { id: "listaId", label: "Lista", type: "select", source: "lists", relation: "lista", required: true },
            { id: "produtoId", label: "Produto", type: "select", source: "products", relation: "produto", required: true },
            { id: "quantidade", label: "Quantidade", type: "number", min: 1, required: true },
            { id: "precoUnitario", label: "Preço unitário (R$)", type: "number", min: 0, step: "0.01", required: true },
            { id: "comprado", label: "Status", type: "select", options: [{ value: "false", label: "Pendente" }, { value: "true", label: "Comprado" }] }
        ], colunas: ["ID", "Lista", "Produto", "Qtd.", "Preço unit.", "Status"]
    }
};

let dados = { users: [], categories: [], products: [], lists: [], items: [] };
let entidadeAtual = "items";
let idEmEdicao = null;

const conteudo = document.getElementById("content");
const feedback = document.getElementById("feedback");
const statusApi = document.getElementById("statusApi");

async function requisicao(url, opcoes = {}) {
    const configuracao = { ...opcoes, headers: { "Content-Type": "application/json", ...(opcoes.headers || {}) } };
    const resposta = await fetch(url, configuracao);
    if (resposta.status === 204) return null;
    const corpo = await resposta.json().catch(() => null);
    if (!resposta.ok) {
        const mensagem = corpo?.mensagem || corpo?.message || `Erro HTTP ${resposta.status}`;
        const detalhes = corpo?.detalhes ? Object.values(corpo.detalhes).join(" ") : "";
        throw new Error(`${mensagem}${detalhes ? " " + detalhes : ""}`);
    }
    return corpo;
}

async function carregarDados() {
    const chaves = Object.keys(configuracoes);
    const resultados = await Promise.all(chaves.map(chave => requisicao(`${API}/${configuracoes[chave].endpoint}`)));
    chaves.forEach((chave, indice) => dados[chave] = resultados[indice]);
    statusApi.classList.add("online");
    statusApi.classList.remove("offline");
    statusApi.innerHTML = '<span class="status-dot"></span>API conectada';
}

function mostrarMensagem(texto, tipo = "success") {
    feedback.textContent = texto;
    feedback.className = `feedback visible ${tipo}`;
    if (tipo !== "error") window.setTimeout(() => feedback.classList.remove("visible"), 4000);
}

function escapar(valor) {
    return String(valor ?? "").replace(/[&<>"']/g, caractere => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    }[caractere]));
}

function valorCampo(campo, registro = {}) {
    if (campo.relation) return registro[campo.relation]?.id ?? "";
    return registro[campo.id] ?? "";
}

function montarCampo(campo, registro = {}) {
    const valor = valorCampo(campo, registro);
    let controle = "";
    if (campo.type === "select") {
        let opcoes = campo.options || (dados[campo.source] || []).map(registro => ({ value: registro.id, label: registro.nome }));
        controle = `<select id="${campo.id}" name="${campo.id}" ${campo.required ? "required" : ""}>`;
        if (campo.optional) controle += '<option value="">Sem categoria</option>';
        else controle += '<option value="">Selecione...</option>';
        opcoes.forEach(opcao => {
            controle += `<option value="${escapar(opcao.value)}" ${String(valor) === String(opcao.value) ? "selected" : ""}>${escapar(opcao.label)}</option>`;
        });
        controle += "</select>";
    } else {
        const tipo = campo.type || "text";
        const min = campo.min !== undefined ? `min="${campo.min}"` : "";
        const step = campo.step !== undefined ? `step="${campo.step}"` : "";
        const required = campo.required ? "required" : "";
        controle = `<input id="${campo.id}" name="${campo.id}" type="${tipo}" value="${escapar(valor)}" ${min} ${step} ${required}>`;
    }
    return `<div class="field"><label for="${campo.id}">${escapar(campo.label)}</label>${controle}</div>`;
}

function renderizar() {
    const config = configuracoes[entidadeAtual];
    conteudo.innerHTML = `
        <div class="panel-heading">
            <div><h2>${config.titulo}</h2><p>${config.descricao}</p></div>
        </div>
        <form id="formRegistro" class="form-card">
            <h3 id="tituloFormulario">Cadastrar ${config.singular}</h3>
            <div class="form-grid">${config.campos.map(campo => montarCampo(campo)).join("")}</div>
            <div class="form-actions">
                <button type="submit" class="primary" id="btnSalvar">Cadastrar</button>
                <button type="button" class="secondary" id="btnCancelar" hidden>Cancelar edição</button>
            </div>
        </form>
        <div class="table-toolbar">
            <strong>Registros cadastrados (${dados[entidadeAtual].length})</strong>
            <input id="filtroTabela" type="search" placeholder="Filtrar registros..." aria-label="Filtrar registros">
        </div>
        <div id="tabelaRegistros"></div>`;
    document.getElementById("formRegistro").addEventListener("submit", salvarRegistro);
    document.getElementById("btnCancelar").addEventListener("click", cancelarEdicao);
    document.getElementById("filtroTabela").addEventListener("input", renderizarTabela);
    renderizarTabela();
}

function dadosLinha(registro) {
    switch (entidadeAtual) {
        case "users": return [registro.id, registro.nome, registro.email];
        case "categories": return [registro.id, registro.nome, registro.descricao || "—"];
        case "products": return [registro.id, registro.nome, registro.descricao || "—", registro.categoria?.nome || "Sem categoria"];
        case "lists": return [registro.id, registro.nome, registro.usuario?.nome || "—", registro.dataCriacao ? new Date(registro.dataCriacao).toLocaleString("pt-BR") : "—"];
        case "items": return [registro.id, registro.lista?.nome || "—", registro.produto?.nome || "—", registro.quantidade, Number(registro.precoUnitario || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" }), registro.comprado ? "Comprado" : "Pendente"];
    }
}

function renderizarTabela() {
    const config = configuracoes[entidadeAtual];
    const filtro = (document.getElementById("filtroTabela")?.value || "").toLowerCase();
    const registros = dados[entidadeAtual].filter(registro => dadosLinha(registro).join(" ").toLowerCase().includes(filtro));
    if (registros.length === 0) {
        document.getElementById("tabelaRegistros").innerHTML = `<div class="empty">${dados[entidadeAtual].length === 0 ? "Nenhum registro cadastrado ainda." : "Nenhum registro corresponde à busca."}</div>`;
        return;
    }
    const cabecalho = config.colunas.map(coluna => `<th>${escapar(coluna)}</th>`).join("");
    const linhas = registros.map(registro => {
        const celulas = dadosLinha(registro).map((valor, indice) => {
            if (entidadeAtual === "items" && indice === 5) return `<td><span class="pill ${registro.comprado ? "" : "pending"}">${escapar(valor)}</span></td>`;
            return `<td>${escapar(valor)}</td>`;
        }).join("");
        return `<tr>${celulas}<td><div class="actions"><button class="secondary small" data-editar="${registro.id}">Editar</button><button class="danger small" data-excluir="${registro.id}">Excluir</button></div></td></tr>`;
    }).join("");
    document.getElementById("tabelaRegistros").innerHTML = `<div class="table-wrap"><table><thead><tr>${cabecalho}<th>Ações</th></tr></thead><tbody>${linhas}</tbody></table></div>`;
    document.querySelectorAll("[data-editar]").forEach(botao => botao.addEventListener("click", () => editarRegistro(Number(botao.dataset.editar))));
    document.querySelectorAll("[data-excluir]").forEach(botao => botao.addEventListener("click", () => excluirRegistro(Number(botao.dataset.excluir))));
}

function montarPayload(formulario) {
    const payload = {};
    configuracoes[entidadeAtual].campos.forEach(campo => {
        const entrada = formulario.elements[campo.id];
        if (campo.type === "select" && campo.options) payload[campo.id] = entrada.value === "true";
        else if (campo.type === "select" || campo.relation) payload[campo.id] = entrada.value === "" ? null : Number(entrada.value);
        else if (campo.type === "number") payload[campo.id] = entrada.value === "" ? null : Number(entrada.value);
        else payload[campo.id] = entrada.value.trim();
    });
    return payload;
}

async function salvarRegistro(evento) {
    evento.preventDefault();
    const formulario = evento.currentTarget;
    if (!formulario.reportValidity()) return;
    const payload = montarPayload(formulario);
    const endpoint = `${API}/${configuracoes[entidadeAtual].endpoint}`;
    try {
        if (idEmEdicao === null) {
            await requisicao(endpoint, { method: "POST", body: JSON.stringify(payload) });
            mostrarMensagem(`${configuracoes[entidadeAtual].singular} cadastrado com sucesso.`);
        } else {
            await requisicao(`${endpoint}/${idEmEdicao}`, { method: "PUT", body: JSON.stringify(payload) });
            mostrarMensagem(`${configuracoes[entidadeAtual].singular} atualizado com sucesso.`);
        }
        idEmEdicao = null;
        await carregarDados();
        renderizar();
    } catch (erro) {
        mostrarMensagem(erro.message, "error");
    }
}

function editarRegistro(id) {
    const registro = dados[entidadeAtual].find(item => item.id === id);
    if (!registro) return;
    idEmEdicao = id;
    const config = configuracoes[entidadeAtual];
    document.getElementById("tituloFormulario").textContent = `Editar ${config.singular} #${id}`;
    document.getElementById("btnSalvar").textContent = "Salvar alterações";
    document.getElementById("btnCancelar").hidden = false;
    document.querySelector(".form-grid").innerHTML = config.campos.map(campo => montarCampo(campo, registro)).join("");
    document.getElementById("formRegistro").scrollIntoView({ behavior: "smooth", block: "start" });
}

function cancelarEdicao() {
    idEmEdicao = null;
    renderizar();
}

async function excluirRegistro(id) {
    const config = configuracoes[entidadeAtual];
    if (!confirm(`Deseja realmente excluir o registro #${id}?`)) return;
    try {
        await requisicao(`${API}/${config.endpoint}/${id}`, { method: "DELETE" });
        await carregarDados();
        renderizar();
        mostrarMensagem(`${config.singular} excluído com sucesso.`);
    } catch (erro) {
        mostrarMensagem(erro.message, "error");
    }
}

async function trocarEntidade(entidade) {
    entidadeAtual = entidade;
    idEmEdicao = null;
    document.querySelectorAll(".nav-button").forEach(botao => botao.classList.toggle("active", botao.dataset.entity === entidade));
    renderizar();
}

document.querySelectorAll(".nav-button").forEach(botao => botao.addEventListener("click", () => trocarEntidade(botao.dataset.entity)));

async function iniciar() {
    try {
        await carregarDados();
        renderizar();
    } catch (erro) {
        statusApi.classList.add("offline");
        statusApi.classList.remove("online");
        statusApi.innerHTML = '<span class="status-dot"></span>API indisponível';
        conteudo.innerHTML = '<div class="empty">Não foi possível carregar os dados. Verifique se a aplicação e o banco de dados estão em execução e tente atualizar a página.</div>';
        mostrarMensagem(erro.message, "error");
    }
}

iniciar();
