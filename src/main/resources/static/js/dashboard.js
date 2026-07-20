document.addEventListener("DOMContentLoaded", function () {
    // 1. User Dashboard Skills Comparison Chart
    var skillsChartCanvas = document.getElementById("skillsChart");
    if (skillsChartCanvas) {
        var matching = parseInt(skillsChartCanvas.getAttribute("data-matching")) || 0;
        var missing = parseInt(skillsChartCanvas.getAttribute("data-missing")) || 0;

        // If both are 0, default to showing 0% progress
        if (matching === 0 && missing === 0) {
            missing = 100;
        }

        new Chart(skillsChartCanvas, {
            type: 'doughnut',
            data: {
                labels: ['Matched Skills', 'Skill Gap'],
                datasets: [{
                    data: [matching, missing],
                    backgroundColor: [
                        '#00f2fe', // Neon Blue
                        'rgba(255, 0, 127, 0.2)' // Neon Pink (Transparent)
                    ],
                    borderColor: [
                        '#00f2fe',
                        '#ff007f'
                    ],
                    borderWidth: 2,
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: '#a0aec0',
                            font: {
                                family: 'Plus Jakarta Sans',
                                size: 12
                            }
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.label + ': ' + context.raw + '%';
                            }
                        }
                    }
                },
                cutout: '75%'
            }
        });
    }

    // 2. Admin Dashboard Job Roles Distribution Chart
    var adminRolesChartCanvas = document.getElementById("adminRolesChart");
    if (adminRolesChartCanvas) {
        var javaCount = parseInt(adminRolesChartCanvas.getAttribute("data-java")) || 0;
        var pythonCount = parseInt(adminRolesChartCanvas.getAttribute("data-python")) || 0;
        var fullstackCount = parseInt(adminRolesChartCanvas.getAttribute("data-fullstack")) || 0;
        var backendCount = parseInt(adminRolesChartCanvas.getAttribute("data-backend")) || 0;
        var frontendCount = parseInt(adminRolesChartCanvas.getAttribute("data-frontend")) || 0;
        var devopsCount = parseInt(adminRolesChartCanvas.getAttribute("data-devops")) || 0;
        var datascientistCount = parseInt(adminRolesChartCanvas.getAttribute("data-datascientist")) || 0;
        var mobileCount = parseInt(adminRolesChartCanvas.getAttribute("data-mobile")) || 0;

        new Chart(adminRolesChartCanvas, {
            type: 'bar',
            data: {
                labels: ['Java Dev', 'Python Dev', 'Full Stack', 'Backend', 'Frontend', 'DevOps', 'Data Sci', 'Mobile'],
                datasets: [{
                    label: 'Number of Users',
                    data: [javaCount, pythonCount, fullstackCount, backendCount, frontendCount, devopsCount, datascientistCount, mobileCount],
                    backgroundColor: [
                        'rgba(0, 242, 254, 0.4)',
                        'rgba(155, 81, 224, 0.4)',
                        'rgba(255, 0, 127, 0.4)',
                        'rgba(0, 242, 254, 0.7)',
                        'rgba(155, 81, 224, 0.7)',
                        'rgba(255, 0, 127, 0.7)',
                        'rgba(0, 242, 254, 0.9)',
                        'rgba(155, 81, 224, 0.9)'
                    ],
                    borderColor: [
                        '#00f2fe',
                        '#9b51e0',
                        '#ff007f',
                        '#00f2fe',
                        '#9b51e0',
                        '#ff007f',
                        '#00f2fe',
                        '#9b51e0'
                    ],
                    borderWidth: 2,
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: 'rgba(255, 255, 255, 0.05)'
                        },
                        ticks: {
                            color: '#a0aec0',
                            stepSize: 1
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        },
                        ticks: {
                            color: '#a0aec0'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    }
});
