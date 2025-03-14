const API_URL = "http://localhost:8080"; // Substitua pela URL do seu back-end

// Listar clientes
function listarClientes() {
    fetch(`${API_URL}/clientes`)
        .then(response => response.json())
        .then(data => {
            const clientesDiv = document.getElementById("clientes-lista");
            clientesDiv.innerHTML = "<ul>" + data.map(cliente => 
                `<li> ${cliente.idCliente} - ${cliente.nome} - ${cliente.email}</li>`).join("") + "</ul>";
        })
        .catch(err => console.error("Erro ao listar clientes:", err));
}

// Listar carros
function listarCarros() {
    fetch(`${API_URL}/carros`)
        .then(response => response.json())
        .then(data => {
            const carrosDiv = document.getElementById("carros-lista");
            carrosDiv.innerHTML = "<ul>" + data.map(carro => 
                `<li>ID Carro: ${carro.idCarro} - ${carro.marca} ${carro.modelo} - Placa: ${carro.placa}` ).join("") + "</ul>";
            
        })
        .catch(err => console.error("Erro ao listar carros:", err));
}

// Listar ordens de serviço
function listarOrdens() {
    fetch(`${API_URL}/ordens`)
        .then(response => response.json())
        .then(data => {
            const ordensDiv = document.getElementById("ordens-lista");
            ordensDiv.innerHTML = "<ul>" + data.map(os => 
                `<li>OS #${os.idOs} - Status: ${os.status} - Total: R$ ${os.valorTotal}</li>`).join("") + "</ul>";
        })
        .catch(err => console.error("Erro ao listar ordens de serviço:", err));
}

// Listar peças
function listarPecas() {
    fetch(`${API_URL}/pecas`)
        .then(response => response.json())
        .then(data => {
            const pecasDiv = document.getElementById("pecas-lista");
            pecasDiv.innerHTML = "<ul>" + data.map(peca => 
                `<li>${peca.nome} - Estoque: ${peca.quantidadeEstoque} - Preço: R$ ${peca.preco}</li>`).join("") + "</ul>";
        })
        .catch(err => console.error("Erro ao listar peças:", err));
}

// Listar serviços
function listarServicos() {
    fetch(`${API_URL}/servicos`)
        .then(response => response.json())
        .then(data => {
            const servicosDiv = document.getElementById("servicos-lista");
            servicosDiv.innerHTML = "<ul>" + data.map(servico => 
                `<li>${servico.descricao} - Preço: R$ ${servico.preco}</li>`).join("") + "</ul>";
        })
        .catch(err => console.error("Erro ao listar serviços:", err));
}
