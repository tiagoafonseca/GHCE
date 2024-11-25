document.getElementById('uploadForm').onsubmit = async (event) => {
    event.preventDefault();

    const fileInput = document.getElementById('csvFile');
    const file = fileInput.files[0];
    const responseMessage = document.getElementById('responseMessage');

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch('http://localhost:8080/api/upload', {method: 'POST', body: formData});

            if (response.ok) {
                const result = await response.json();
                console.log(result); // Verifica o formato dos dados
                responseMessage.innerHTML = `<p style="color:black;">Horário Carregado com sucesso!</p>`;
                renderTable(result)  //Construir a tabela
            } else {
                const errorText = await response.text();
                responseMessage.innerHTML = `<p style="color:red;">Erro ao processar o arquivo: ${errorText}</p>`;
            }
        } catch (error) {
            console.error('Erro:', error);
            responseMessage.innerHTML = `<p style="color:red;">Erro ao enviar o arquivo. Verifique a conexão com o servidor.</p>`;
        }
    } else {
        alert('Por favor, selecione um arquivo.');
    }
};


// Construção da Tabela
function renderTable(data) {
    const tableHeader = document.getElementById('tableHeader');
    const tableBody = document.getElementById('tableBody');

    tableHeader.innerHTML = "";
    tableBody.innerHTML = "";

    if (data.length > 0) {
        // Cria os cabeçalhos da tabela
        const headers = Object.keys(data[0]); // Pega os nomes das colunas
        const headerRow = document.createElement('tr');
        headers.forEach(header => {
            const th = document.createElement('th');
            th.textContent = header; // Adiciona os cabeçalhos das colunas
            headerRow.appendChild(th);
        });
        tableHeader.appendChild(headerRow);

        // Cria as linhas da tabela
        data.forEach(row => {
            const tr = document.createElement('tr');
            headers.forEach(header => {
                const td = document.createElement('td');
                td.textContent = row[header] || ""; // Preenche as células com os valores
                tr.appendChild(td);
            });
            tableBody.appendChild(tr);
        });
    }
}


// Função para sanitizar HTML dinâmico
//function sanitizeHTML(str) {
//    const tempDiv = document.createElement('div');
//    tempDiv.textContent = str;
//    return tempDiv.innerHTML;
//}