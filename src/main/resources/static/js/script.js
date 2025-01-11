// Lógica do formulário de upload
document.getElementById('uploadForm').onsubmit = async (event) => {
    event.preventDefault();

    let tableHeader = document.getElementById('tableHeader');
    let tableBody = document.getElementById('tableBody');
    let data="";
    this.currentSection=0;
    let lastSection=0;


    const fileInput = document.getElementById('csvFile');
    const file = fileInput.files[0];
    const responseMessage = document.getElementById('responseMessage');
    const nome = document.getElementById("nomeHorario").value


    if (file) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('nome',nome)
        try {
            const qualidade = await fetch('http://localhost:8080/api/upload', { method: 'POST', body: formData });

            if (qualidade.ok) {
                const responseData = await fetch('http://localhost:8080/api/json'); // Fetch the JSON from the Spring Boot endpoint
                const horarioRecebido = await responseData.json();
                this.data=horarioRecebido;

                console.log("Aqui crl"+this.data.qualidade.horarioPontuacao);
                document.getElementById('scheduleQuality').textContent = `${this.data.qualidade.horarioPontuacao} pontos`;
                console.log(horarioRecebido); // Verifica o formato dos dados
                console.log("qualidade"+this.data.qualidade.horarioPontuacao)
                responseMessage.innerHTML = `<p style="color:black;">Horário Carregado com sucesso!</p>`;

                //renderTable(jsonResponse); // Construir a tabela
                determineLastSection();
                renderHeaders();
                runSection(0);

                // Atualizar a qualidade do horário após o upload
                //fetchScheduleQuality();
            } else {
                const errorText = await response.text();
                responseMessage.innerHTML = `<p style="color:red;">Erro ao processar o arquivo: ${errorText}</p>`;
                document.getElementById('scheduleQuality').textContent = "Erro ao obter qualidade.";
            }
        } catch (error) {
            console.error('Erro:', error);
            responseMessage.innerHTML = `<p style="color:red;">Erro ao enviar o arquivo. Verifique a conexão com o servidor.</p>`;
            document.getElementById('scheduleQuality').textContent = "Erro ao obter qualidade.";
        }
    } else {
        alert('Arquivo e nome são campos obrigatorios.');
    }
};




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

}

function renderHeaders(){
    const headers = ["Curso", "Unidade de Execução", "Turno", "Turma", "Inscritos no Turno", "Dia da Semana", "Início", "Fim", "Dia", "Caracteristicas da Sala Pedida", "Sala de Aula", "Lotação", "Caracteristicas Reais da Sala Pedida","ID"];
    console.log("Headers  " + headers);

    const headerRow = document.createElement('tr');
    headers.forEach(header => {
        const th = document.createElement('th');
        th.textContent = header; // Adiciona os cabeçalhos das colunas
        headerRow.appendChild(th);
    });
    tableHeader.appendChild(headerRow);
    this.data.aulas=data.aulas.slice(1);
}


function determineLastSection(){
    this.lastSection=Math.ceil(this.data.aulas.length/500);
    console.log(this.lastSection);
}

function updateCurrentPage(){
    let pageId = document.getElementById('currentSection');
    pageId.innerHTML = this.currentSection;
}




function nextPage(){
    if(this.currentSection!=this.lastSection){
        this.currentSection++;
        runSection(this.currentSection);
    }
    updateCurrentPage();
    console.log(this.currentSection)
}

function backPage(){
    if(this.currentSection!=0){
        this.currentSection--;
        runSection(this.currentSection);
    }
    updateCurrentPage();
    console.log(this.currentSection)
}




function runSection(section){
    var id=(section*500);
    clearCurrentTable();
    for (let i = (section*500); i < 500+(section*500); i++) {
        console.log(data.aulas[i]);
        const tr = document.createElement('tr');
        Object.keys(data.aulas[i]).forEach(linha => {
            const td = document.createElement('td');
            td.textContent = data.aulas[i][linha] || ""; // Preenche as células com os valores
            tr.appendChild(td);
        });
        //Adicionar o ID
        const td = document.createElement('td');
        td.textContent = id.toString();
        tr.appendChild(td);
        tableBody.appendChild(tr);
        id++;

    }

}

function clearCurrentTable(){
    tableBody.innerHTML = "";
    console.log("Limpei");
}



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

