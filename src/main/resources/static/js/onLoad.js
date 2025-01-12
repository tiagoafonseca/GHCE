renderHorarios();

async function renderHorarios() {
    const caixa = document.getElementById("caixaGrandeOndeMetoOsHorarios");

    // Fetch data from the API
    try {
        const response = await fetch('http://localhost:8080/api2/recieveListOfHorários');
        const data = await response.json();

        // Extract data arrays
        const { nomes, qualidades, datas } = data;

        // Log the data for debugging
        console.log('Nomes:', nomes);
        console.log('Qualidades:', qualidades);
        console.log('Datas:', datas);

        // Validate data lengths
        if (!nomes || !qualidades || !datas || nomes.length !== qualidades.length || nomes.length !== datas.length) {
            console.error("Data arrays are not aligned or missing.");
            return;
        }

        // Render the elements
        nomes.forEach((nome, index) => {
            console.log("Ando por aqui");

            const div = document.createElement('div');
            div.classList.add("horariosContainer");

            const pQualidade = document.createElement('p');
            pQualidade.innerHTML = qualidades[index];
            pQualidade.classList.add("p");

            const pNome = document.createElement('p');
            pNome.innerHTML = nome;
            pNome.classList.add("p");

            const pData = document.createElement('p');
            pData.innerHTML = datas[index];
            pData.classList.add("p");

            div.append(pQualidade, pNome, pData);
            caixa.append(div);
        });

    } catch (error) {
        console.error('Error fetching or processing data:', error);
    }
}

