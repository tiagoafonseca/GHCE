// Função para buscar a pontuação do horário
async function fetchScheduleQuality() {
    try {
        const response = await fetch('http://localhost:8080/api1/horarioPontuacao');
        if (response.ok) {
            const quality = await response.json();
            document.getElementById('scheduleQuality').textContent = `${quality} pontos`; // Atualiza o HTML
        } else {
            document.getElementById('scheduleQuality').textContent = "Erro ao obter qualidade.";
        }
    } catch (error) {
        console.error('Erro ao buscar a pontuação do horário:', error);
        document.getElementById('scheduleQuality').textContent = "Erro ao obter qualidade.";
    }
}

// Construção da Tabela
function renderTable(data) {
    const tableHeader = document.getElementById('tableHeader');
    const tableBody = document.getElementById('tableBody');

    tableHeader.innerHTML = "";
    tableBody.innerHTML = "";
    console.log("First Checkpoint")
    console.log(data);

    // Cria os cabeçalhos da tabela
    const headers2 = ["Curso", "Unidade de Execução", "Turno", "Turma", "Inscritos no Turno", "Dia da Semana", "Início", "Fim", "Dia", "Caracteristicas da Sala Pedida", "Sala de Aula", "Lotação", "Caracteristicas Reais da Sala Pedida"];
    const headers = Object.keys(data[0]); // Pega os nomes das colunas
    console.log("Headers  " + headers);
    const headerRow = document.createElement('tr');
    headers2.forEach(header => {
        const th = document.createElement('th');
        th.textContent = header; // Adiciona os cabeçalhos das colunas
        headerRow.appendChild(th);
    });
    tableHeader.appendChild(headerRow);

    // Cria as linhas da tabela
    let firstTime = false;
    data.forEach(row => {
        if (firstTime) {
            const tr = document.createElement('tr');
            headers.forEach(header => {
                const td = document.createElement('td');
                td.textContent = row[header] || ""; // Preenche as células com os valores
                tr.appendChild(td);
            });
            tableBody.appendChild(tr);
        }
        firstTime = true;
    });
}

// Lógica do formulário de upload
document.getElementById('uploadForm').onsubmit = async (event) => {
    event.preventDefault();

    const fileInput = document.getElementById('csvFile');
    const file = fileInput.files[0];
    const responseMessage = document.getElementById('responseMessage');
    const nome = document.getElementById("nomeHorario").value


    if (file) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('nome',nome)
        try {
            const response = await fetch('http://localhost:8080/api/upload', { method: 'POST', body: formData });

            if (response.ok) {
                const responseData = await fetch('http://localhost:8080/api/json'); // Fetch the JSON from the Spring Boot endpoint
                const jsonResponse = await responseData.json();
                console.log(jsonResponse); // Verifica o formato dos dados
                responseMessage.innerHTML = `<p style="color:black;">Horário Carregado com sucesso!</p>`;
                renderTable(jsonResponse); // Construir a tabela

                // Atualizar a qualidade do horário após o upload
                fetchScheduleQuality();
            } else {
                const errorText = await response.text();
                responseMessage.innerHTML = `<p style="color:red;">Erro ao processar o arquivo: ${errorText}</p>`;
            }
        } catch (error) {
            console.error('Erro:', error);
            responseMessage.innerHTML = `<p style="color:red;">Erro ao enviar o arquivo. Verifique a conexão com o servidor.</p>`;
        }
    } else {
        alert('Arquivo e nome são campos obrigatorios.');
    }
};

// Adicionar evento ao botão "Recalcular Qualidade"
document.getElementById('recalculateQualityButton').addEventListener('click', async () => {
    // Obter o estado das checkboxes
    const includeOvercrowding = document.getElementById('metricOvercrowding').checked;
    const includeNoRoom = document.getElementById('metricNoRoom').checked;

    try {
        // Enviar as opções para o backend
        const response = await fetch(`http://localhost:8080/api/evaluateOvercrowding?overcrowding=${includeOvercrowding}&noRoom=${includeNoRoom}`);

        if (response.ok) {
            const quality = await response.json();
            document.getElementById('scheduleQuality').textContent = `${quality} pontos`; // Atualiza o HTML
        } else {
            document.getElementById('scheduleQuality').textContent = "Erro ao recalcular qualidade.";
        }
    } catch (error) {
        console.error('Erro ao recalcular a pontuação:', error);
        document.getElementById('scheduleQuality').textContent = "Erro ao recalcular qualidade.";
    }
});

