// Lógica do formulário de upload

this.currentSection=0;
this.idDoHorarioSelecionado="";
this.nomeHorarioSelecionado="";
this.headersLoaded=false;

document.getElementById('uploadForm').onsubmit = async (event) => {
    event.preventDefault();
    let tableHeader = document.getElementById('tableHeader');
    let tableBody = document.getElementById('tableBody');
    let data="";
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
                if(!headersLoaded) {
                    renderHeaders();
                    headersLoaded=true;
                }
                runSection(0);
                document.getElementById("nomeHorariobig").innerHTML=this.data.name;
                renderHorarios();
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



function renderHeaders(){
    const headers = ["ID", "Curso","Unidade de Execução", "Turno", "Turma", "Inscritos no Turno", "Dia da Semana", "Início", "Fim", "Dia", "Caracteristicas da Sala Reais", "Sala de Aula", "Lotação", "Caracteristicas Reais da Sala Pedida"];
    console.log("Headers  " + headers);

    const headerRow = document.createElement('tr');
    headers.forEach(header => {
        const th = document.createElement('th');
        th.textContent = header; // Adiciona os cabeçalhos das colunas
        headerRow.appendChild(th);
    });
    tableHeader.appendChild(headerRow);

}


function determineLastSection(){
    this.lastSection=Math.ceil(this.data.aulas.length/500)-1;
    document.getElementById("ultimaPágina").innerHTML= this.lastSection;
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
    this.currentSection=section;
    updateCurrentPage();
    clearCurrentTable();
    for (let i = (section*500); i < 500+(section*500); i++) {
        console.log(data.aulas[i]);
        const tr = document.createElement('tr');
        Object.keys(data.aulas[i]).forEach(linha => {
            const td = document.createElement('td');
            td.textContent = data.aulas[i][linha] || ""; // Preenche as células com os valores
            tr.appendChild(td);
        });

        tableBody.appendChild(tr);


    }

}



function changePlaceHolder(){


}



async function changeAttribute() {
    var idAula = document.getElementById("idDaAula").value;
    var idReal=idAula-1;
    var select = document.getElementById("atriubutos");
    var info = document.getElementById("info").value;
    const idAtributo = select.value;

    const formData = new FormData();
    formData.append('idAula', idReal);
    formData.append('idParametro', idAtributo);
    formData.append('newInfo', info);
    console.log(idAula)
    console.log(idAtributo)
    console.log(info)

    var response = await fetch('http://localhost:8080/api2/editLine', {method: 'POST', body: formData});
    if (!response.ok) {
        console.error('Erro ao editar');
     }
        switch (idAtributo) {
            case "1":
                data.aulas[idReal].curso = info;
                console.log("Aqui")
                runSection(currentSection);
                break;
            case "2":
                data.aulas[idReal].unidadeDeExecucao = info;
                console.log("Aqui 2")
                runSection(currentSection);
                break;
            case "3":
                data.aulas[idReal].turno = info;
                console.log("Aqui 3")
                runSection(currentSection);
                break;
            case "4":
                data.aulas[idReal].turma = info;
                runSection(currentSection);
                break;
            case "5":
                data.aulas[idReal].inscritosNoTurno = info;
                runSection(currentSection);
                console.log("Aqui 3")
                break;
            case "6":
                data.aulas[idReal].diaDaSemana = info;
                runSection(currentSection);
                break;
            case "7":
                data.aulas[idReal].inicio = info;
                runSection(currentSection);
                break;
            case "8":
                data.aulas[idReal].fim = info;
                runSection(currentSection);
                break;
            case "9":
                data.aulas[idReal].dia = info;
                runSection(currentSection);
                break;
            case "10":
                data.aulas[idReal].caracteristicasDaSalaPedidaParaAula = info;
                runSection(currentSection);
                break;
            case "11":
                data.aulas[idReal].salaDaAula = info;
                runSection(currentSection);
                break;
            default:
                console.log("não entrei chefe")

        }
    }


    function clearCurrentTable() {
        tableBody.innerHTML = "";
        console.log("Limpei");
    }


// Adicionar evento ao botão "Recalcular Qualidade"
// document.getElementById('recalculateQualityButton').addEventListener('click', async () => {
    // Obter o estado das checkboxes
//   const includeOvercrowding = document.getElementById('metricOvercrowding').checked;
//  const includeNoRoom = document.getElementById('metricNoRoom').checked;

//  try {
    // Enviar as opções para o backend
//      const response = await fetch(`http://localhost:8080/api/evaluateOvercrowding?overcrowding=${includeOvercrowding}&noRoom=${includeNoRoom}`);

//      if (response.ok) {
//          const quality = await response.json();
//          document.getElementById('scheduleQuality').textContent = `${quality} pontos`; // Atualiza o HTML
//      } else {
//          document.getElementById('scheduleQuality').textContent = "Erro ao recalcular qualidade.";
//      }
//  } catch (error) {
//      console.error('Erro ao recalcular a pontuação:', error);
//      document.getElementById('scheduleQuality').textContent = "Erro ao recalcular qualidade.";
//  }
// });


    let IdHorarioSelecionado = null;  // variável global que guarda o ID do horário selecionado
    let nomeHorarioSelecionado = "";  // variável global que guarda o Nome do horário selecionado

    function changeSelectID(id) {
        const div = document.getElementById(id);
        const specificParagraph = div.querySelector('#nomeHorario');
        this.nomeHorarioSelecionado = specificParagraph.innerHTML;
        IdHorarioSelecionado = id;
        nomeHorarioSelecionado = this.nomeHorarioSelecionado;
        console.log("Selecionei isto: " + this.nomeHorarioSelecionado);
    }


    async function LoadHorárioSelecionado() {
        const formData = new FormData();
        formData.append('nomeHorario', this.nomeHorarioSelecionado);
        console.log("Enviei " + this.nomeHorarioSelecionado);
        var horario = await fetch('http://localhost:8080/api2/getSelectedHorario', {method: 'POST', body: formData});
        horarioObjeto = await horario.json();
        this.data = horarioObjeto;
        this.data.aulas = data.aulas.slice(1);
        if (!this.headersLoaded) {
            renderHeaders();
            this.headersLoaded = true;
        }
        determineLastSection();
        runSection(0);
        document.getElementById("nomeHorariobig").innerHTML = this.data.name;

    }

// BUG - ao dar refresh o horário volta
    /* async function ApagarHorarioDaLista() {
        if (!IdHorarioSelecionado) {
            alert('Nenhum horário selecionado!');
            return;
        }
        const elemento = document.getElementById(IdHorarioSelecionado);
        elemento.remove();
        IdHorarioSelecionado = null;
        console.log('Horário removido.');
    } */

    async function ApagarHorarioDaLista() {
        if (!IdHorarioSelecionado) {
            alert('Nenhum horário selecionado!');
            return;
        }

        const nomeHorario = nomeHorarioSelecionado;
        console.log(`Tentando apagar o horário: ${nomeHorario}`);

        try {
            const response = await fetch('http://localhost:8080/api2/deleteHorario', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({nomeHorario}),
            });

            if (response.ok) {
                const elemento = document.getElementById(IdHorarioSelecionado);
                elemento.remove();
                IdHorarioSelecionado = null;
                nomeHorarioSelecionado = null;
                console.log(`Horário ${nomeHorario} apagado com sucesso!`);
            } else {
                const errorMessage = await response.text();
                console.error(`Erro ao apagar o horário: ${errorMessage}`);
                alert(`Erro: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Erro ao conectar ao servidor:', error);
            alert('Erro ao conectar ao servidor.');
        }
    }

    async function fetchMapaErros() {
        let metrica1 = null;
        let metrica2 = null;
        let metrica3 = null;
        let metrica4 = null;
        let metrics = [];

        try {
            const response = await fetch('http://localhost:8080/api1/getMapaErros');
            if (response.ok) {
                const mapaErros = await response.json();
                console.log('Mapa de Erros:', mapaErros);

                // Iterar sobre o mapaErros
                for (const [id, erros] of Object.entries(mapaErros)) {
                    console.log(`ID da Aula: ${id}`);
                    console.log('Erros:');
                    erros.forEach(erro => {
                        if (erro === 1) {
                            metrica1 += 1;
                        } else if (erro === 2) {
                            metrica2 += 2;
                        } else if (erro === 3) {
                            metrica3 += 1;
                        } else if (erro === 4) {
                            metrica4 += 1;
                        }
                        console.log("Métricas Contadas: ", metrica1, metrica2, metrica3, metrica4)
                    });
                }
                metrics = [metrica1, metrica2, metrica3, metrica4];
                return metrics;

            } else {
                console.error('Erro ao buscar mapa de erros:', response.status);
            }
        } catch (error) {
            console.error('Erro de conexão:', error);
        }

}