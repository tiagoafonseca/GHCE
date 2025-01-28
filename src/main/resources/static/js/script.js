// Lógica do formulário de upload

this.currentSection=0;
this.idDoHorarioSelecionado="";
this.nomeHorarioSelecionado="";
this.headersLoaded=false;
aulasEmSb=false;
aulasSemSala=false;
aulasAoSab=false;
missingInfo=false;
this.displayBoolean=false;

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
                document.getElementById('scheduleQuality').textContent = `${this.data.qualidade.horarioPontuacao} %`;
                console.log(horarioRecebido); // Verifica o formato dos dados
                console.log("qualidade"+this.data.qualidade.horarioPontuacao)
                responseMessage.innerHTML = `<p style="color:black;">Horário Carregado com sucesso!</p>`;
                changeDisplayifItemMenus();
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
    const headers = ["ID", "Curso","Unidade de Execução", "Turno", "Turma", "Inscritos no Turno", "Dia da Semana", "Início", "Fim", "Dia", "Caracteristicas da Sala Pedida", "Sala de Aula", "Lotação", "Caracteristicas Reais da Sala Pedida"];
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

function clearCurrentTable(){
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

function changeSelectID(id){
    const div = document.getElementById(id);
    const specificParagraph = div.querySelector('#nomeHorario');
    this.nomeHorarioSelecionado=specificParagraph.innerHTML;
    IdHorarioSelecionado = id;
    nomeHorarioSelecionado = this.nomeHorarioSelecionado;
    console.log("Selecionei isto: " + this.nomeHorarioSelecionado);
}

async function LoadHorárioSelecionadoV2() {
    const formData = new FormData();
    formData.append('nomeHorario', data.name);
    console.log("Enviei "+this.nomeHorarioSelecionado);
    var horario = await fetch('http://localhost:8080/api2/getSelectedHorario', {method: 'POST', body: formData});
    horarioObjeto = await horario.json();
    this.data=horarioObjeto;
    setQualidade(data.qualidade.horarioPontuacao+"%")
    //this.data.aulas=data.aulas.slice(1);
    changeDisplayifItemMenus();
    if(!this.headersLoaded){
        renderHeaders();

        this.headersLoaded=true;
    }
    determineLastSection();
    runSection(0);
    document.getElementById("nomeHorariobig").innerHTML=this.data.name;

}



async function LoadHorárioSelecionado() {
    const formData = new FormData();
    formData.append('nomeHorario', this.nomeHorarioSelecionado);
    console.log("Enviei "+this.nomeHorarioSelecionado);
    var horario = await fetch('http://localhost:8080/api2/getSelectedHorario', {method: 'POST', body: formData});
    horarioObjeto = await horario.json();
    this.data=horarioObjeto;
    setQualidade(data.qualidade.horarioPontuacao+"%")
    //this.data.aulas=data.aulas.slice(1);
    changeDisplayifItemMenus();
    if(!this.headersLoaded){
        renderHeaders();

        this.headersLoaded=true;
    }
    determineLastSection();
    runSection(0);
    document.getElementById("nomeHorariobig").innerHTML=this.data.name;

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



function paintErros(){
    clearCss()
    const table = document.getElementById("tabela");
    let rows = table.querySelector("tbody").getElementsByTagName("tr");
    console.log("Pagina atual: "+currentSection);
    for(let i=(currentSection * 500); i<499+(currentSection*500); i++ ){
        let erros=data.qualidade.mapaErros[i];
        let id=false;
        if(erros.includes(1) && aulasEmSb){
            id=true;
             paintSobreLotaçao(rows[i-(currentSection*500)])
        }
        else if(erros.includes(2) && aulasSemSala){
            id=true;
            console.log(i);
            paintSemSala(rows[i-(currentSection*500)])
        }
        else if(erros.includes(3) && missingInfo){

            id=true;
            paintMissingInfo(rows[i-(currentSection*500)])
        }
        else if(erros.includes(5) && aulasAoSab){

            id=true;
            paintAulasAoSabado(rows[i-(currentSection*500)])
        }
        if(id){
            paintId(rows[i-(currentSection*500)])
        }
        id=false;
    }

}

function paintSobreLotaçao(row){
    let element= row.getElementsByTagName("td")[12];
    element.style.backgroundColor= 'rgba(253,39,39,0.69)';
}

function paintId(row){
    let element= row.getElementsByTagName("td")[0];
    element.style.backgroundColor= 'rgba(253,39,39,0.69)';
}

function paintAulasAoSabado(row){
    let element= row.getElementsByTagName("td")[6];

    element.style.backgroundColor= 'rgba(253,39,39,0.69)';
}

function paintSemSala(row){
    console.log(row);
    let element= row.getElementsByTagName("td")[11];
    element.style.backgroundColor= 'rgba(253,39,39,0.69)';
}


//Ta bem
function paintMissingInfo(row){
    row.style.backgroundColor= 'rgba(255,249,0,0.65)';
}

function setQualidade(qualidade){
    var qualidadeD=document.getElementById("scheduleQuality");
    qualidadeD.innerHTML=qualidade;
}

function clearCss(){
    const table = document.getElementById("tabela");
    let rows = table.querySelector("tbody").getElementsByTagName("tr");
    console.log("Pagina atual: "+currentSection);
    for(let i=(currentSection * 500); i<499+(currentSection*500); i++ ){
        for(let j=0; j<14; j++){
            let element= rows[i-(currentSection*500)].getElementsByTagName("td")[j];
            if(i%2==0)
                element.style.backgroundColor= '#fff';
            else
                element.style.backgroundColor= '#b8b8b8';


        }
    }
    }


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
            body: new URLSearchParams({ nomeHorario }),
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



async function AuxGerarGraficosMetricas() {
    try {
        const response = await fetch('http://localhost:8080/api1/getMetricas');
        if (response.ok) {
            const listaDeArraysMetricasRecebido = await response.json();
            const numeroGraficosCarregados = listaDeArraysMetricasRecebido.length;
            console.log('Número de Métricas Recebidas:', listaDeArraysMetricasRecebido);  // Verificar o conteúdo do array retornado
            criarGraficosRadar(numeroGraficosCarregados, listaDeArraysMetricasRecebido);
        }
    } catch (error) {
        console.error('Erro de conexão:', error);
    }
}

function criarGraficosRadar(numHorariosCarregados, listaDeArraysMetricas) {
    // Valida se o canvas está presente
    const ctx = document.getElementById('radarChart');
    if (!ctx) {
        console.error('Canvas com ID "radarChart" não foi encontrado.');
        return;
    }

    // Valida se o número de horários corresponde ao tamanho da lista de métricas
    if (numHorariosCarregados !== listaDeArraysMetricas.length) {
        console.error('O número de horários carregados não corresponde ao tamanho da lista de métricas.');
        return;
    }

    // Cria datasets para cada array na lista
    const datasets = listaDeArraysMetricas.map((arrayMetricas, index) => ({
        label: `Horário ${index + 1}`, // Rótulo para identificar cada dataset
        fill: true,
        data: arrayMetricas,
        backgroundColor: `rgba(${Math.floor(Math.random() * 255)}, 100, 200, 0.2)`, // Cor aleatória para cada dataset
        borderColor: `rgba(${Math.floor(Math.random() * 255)}, 50, 100, 1)`,
        borderWidth: 2,
    }));

    // Configuração dos dados para o gráfico
    const data = {
        labels: ['Sobrelotação', 'Sem sala Atribuída', 'Informação em Falta','Aulas ao Sábado'],
        datasets: datasets, // Adiciona todos os datasets
    };

    // Configuração do gráfico
    const config = {
        type: 'radar',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: true,
            scales: {
                r: {
                    pointLabels: {
                        color: 'black',
                        font: { size: 12, weight: 'bold' },
                        padding: 10,
                    },
                    grid: {
                        lineWidth: 2,
                    },
                    angleLines: {
                        display: false,
                    },
                    suggestedMin: 0,
                    suggestedMax: 100,
                    ticks: {
                        stepSize: 10,
                    },
                },
            },
        },
    };

    // Renderiza o gráfico
    new Chart(ctx.getContext('2d'), config);
}


async function changeAttribute() {
    var idAula = document.getElementById("idDaAula").value;
    var idReal=idAula;
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

function changeDisplayifItemMenus() {
    if (!this.displayBoolean) {
    const div=document.getElementById('itemsMenu');
    div.classList.toggle('buttonsDivOff');
    div.classList.toggle('buttonsDivOn');
        this.displayBoolean=true;
    }
}


function changeState(div){
    console.log("Tou aqui");
    div.classList.toggle('divOn');
    div.classList.toggle('divOff');
    console.log(div.id);
    switch (div.id){
        case "aulasEmSb":
            aulasEmSb=!aulasEmSb;
            console.log(aulasEmSb)
            break
        case "aulasSemSala":
            aulasSemSala=!aulasSemSala;
            break
        case "aulasAoSab":
            aulasAoSab=!aulasAoSab;
            break
        case "missingInfo":
            missingInfo=!missingInfo;
            break



    }
}

async function updateQualidade() {
    await fetch('http://localhost:8080/api2/updateQualidade');
    //LoadHorárioSelecionadoV2();
    alert("Atualizado com Sucesso!");
}

// Chama a função automaticamente ao carregar a página
//window.onload = AuxGerarGraficosMetricas;
