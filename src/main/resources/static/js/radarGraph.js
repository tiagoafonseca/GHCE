document.addEventListener("DOMContentLoaded", () => {
    const ctx = document.getElementById('radarChart').getContext('2d');
    const data = {
        labels: ['Speed', 'Strength', 'Stamina', 'Skill', 'Agility'],
        datasets: [{
            label: 'Athlete Stats 1',
            fill: true,
            data: [65, 59, 90, 81, 56],
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 2
        }, {
            label: 'Athlete Stats 2',
            data: [28, 48, 40, 19, 96],
            fill: true,
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgb(54, 162, 235)',
            borderWidth: 2
        }]
    };
    const config = {
        type: 'radar',
        data: data,
        options: {
            responsive: true,
            maintainAspectRatio: true, // Mantém a proporção do gráfico
            scales: {
                r: {
                    pointLabels: {
                        borderRadius: 2,
                        color: 'black',
                        font: 'bold',
                        padding: 10
                    },

                    grid: {
                        lineWidth: 2
                    },

                    angleLines: {
                        display: false,
                    },

                    suggestedMin: 50,
                    suggestedMax: 100,

                    ticks: {
                        stepSize: 10
                    }
                }
            }
        }
    };
    new Chart(ctx, config);
});
