// main.js start -------------------------------------------------------------------------------------------------------

$(document).ready(function () {

// URLs ----------------------------------------------------------------------------------------------------------------

    const networksURL = "http://localhost:8080/v1/networks";

    const networkAnomaliesURL = "http://localhost:8081/v1/network-anomalies";

    const networkSummariesURL = "http://localhost:8081/v1/network-summaries";

// Initialize animations -----------------------------------------------------------------------------------------------

    const initAnimations = function () {
        $(".navbar a, footer a[href='#myPage']").off().on("click", function (event) {
            if (this.hash !== "") {
                event.preventDefault();
                const hash = this.hash;
                $("html, body").animate({
                    scrollTop: $(hash).offset().top
                }, 900, function () {
                    window.location.hash = hash;
                });
            }
        });

        $(window).scroll(function () {
            $(".slideanim").each(function () {
                const pos = $(this).offset().top;
                const winTop = $(window).scrollTop();
                if (pos < winTop + 600) {
                    $(this).addClass("slide");
                }
            });
        });
    };

    initAnimations();

// Initialize footer date ----------------------------------------------------------------------------------------------

    const initFooterDate = function () {
        const date = new Date();
        $("#footer_text").html(`&copy;<br/>TUS<br/>${date.getFullYear()}`);
    };

    initFooterDate();

// Search network summary by network ID --------------------------------------------------------------------------------

    const getNetworkSummaryByNetworkId = function (networkId) {
        console.log("[INFO] Fetching network summary for network ID:", networkId);
        $.ajax({
            type: "GET",
            url: `${networkSummariesURL}/network/${networkId}`,
            dataType: "json",
            success: function (data, status, jqXHR) {
                console.info(`[SUCCESS] Fetched network summary from: ${networkSummariesURL}/${networkId}`);
                console.debug("[DEBUG] Raw data received:", data);

                if (Array.isArray(data) && data.length > 0) {
                    const networkSummary = data[0];
                    console.debug("[DEBUG] Network summary extracted:", networkSummary);
                    renderNetworkSummary(networkSummary);
                } else {
                    console.warn("[WARN] No network summary data found for network ID:", networkId);
                    $("#modal_title").text("Search Network Summary Error");
                    $("#modal_contents").html(`<h5>No network summary available.</h5>`);
                    $("#my_modal").modal("show");
                }
            },
            error: function (jqXHR, status, error) {
                console.error(`[ERROR] Failed to fetch network summary for network ID: ${networkId}`, {
                    status: status,
                    error: error,
                    responseText: jqXHR.responseText
                });

                $("#modal_title").text("Search Network Summary Error");
                $("#modal_contents").html(`<h5>Network summary not found.</h5>`);
                $("#my_modal").modal("show");
            }
        });
    };

    const renderNetworkSummary = function (networkSummary) {
        $("#summary_hidden_id").val(networkSummary.id);
        $("#total_traffic_volume_input").val(networkSummary.trafficSizeInBytes.toFixed(2));
        $("#anomaly_count_input").val(networkSummary.anomalyCount);
        $("#non_anomaly_count_input").val(networkSummary.nonAnomalyCount);
        $("#last_updated_input").val(networkSummary.lastUpdatedAt);
        $("#modal_title_summary").text(`Summary for Network ${networkSummary.networkId}`);
        $("#my_modal_summary").modal("show");
    };

    $("#search_summary_button").off().on("click", function () {
        const networkIdEntry = $("#search_summary_entry").val().trim();
        if (!networkIdEntry || isNaN(networkIdEntry)) {
            console.warn("[WARN] Invalid network ID entered:", networkIdEntry);
            $("#modal_title").text("Invalid Network ID");
            $("#modal_contents").html(`<h5>Please enter a valid numeric network ID.</h5>`);
            $("#my_modal").modal("show");
        } else {
            getNetworkSummaryByNetworkId(parseInt(networkIdEntry, 10));
        }
    });

// Network anomalies table ---------------------------------------------------------------------------------------------

    const renderNetworkAnomaliesTable = function (networkAnomalies) {
        const networkAnomaliesTable = $("#anomalies_table_id").DataTable();
        networkAnomaliesTable.clear().draw();
        networkAnomaliesTable.search("").draw();

        networkAnomalies.forEach(networkAnomaly => {
            networkAnomaliesTable.row.add([
                networkAnomaly.networkId,
                networkAnomaly.sizeInBytes.toFixed(2),
                networkAnomaly.timestamp,
                `<button class="delete-anomaly-button btn btn-danger" id="${networkAnomaly.id}">Delete</button>`
            ]);
        });

        networkAnomaliesTable.draw();
    };

    const getNetworkAnomaliesAndUpdateTable = function () {
        console.log("[INFO] Fetching network anomalies");
        $.ajax({
            type: "GET",
            url: `${networkAnomaliesURL}`,
            dataType: "json",
            success: function (data, status, jqXHR) {
                console.info(`[SUCCESS] Fetched network anomalies from: ${networkAnomaliesURL}`);
                console.debug("[DEBUG] Raw network anomalies data received:", data);

                if (Array.isArray(data) && data.length > 0) {
                    renderNetworkAnomaliesTable(data);
                } else {
                    console.warn("[WARN] No network anomalies data received.");
                    $("#modal_title").text("Network Anomalies Data Error");
                    $("#modal_contents").html(`<h5>No network anomalies found.</h5>`);
                    $("#my_modal").modal("show");
                }
            },
            error: function (jqXHR, status, error) {
                console.error(`[ERROR] Failed to fetch network anomalies`, {
                    status: status,
                    error: error,
                    responseText: jqXHR.responseText
                });

                $("#modal_title").text("Network Anomalies Fetch Error");
                $("#modal_contents").html(`<h5>Error Status: ${jqXHR.status}</h5>`);
                $("#my_modal").modal("show");
            }
        });
    };

    const initNetworkAnomaliesTable = function () {
        const networkAnomaliesTable = $("#anomalies_table_id").DataTable();
        networkAnomaliesTable.off("click", ".delete-anomaly-button")
            .on("click", ".delete-anomaly-button", function () {
                const networkAnomalyId = $(this).attr("id");
                deleteNetworkAnomalyById(networkAnomalyId);
            });
    };

    initNetworkAnomaliesTable();
    getNetworkAnomaliesAndUpdateTable();

    $("#anomalies-refresh").off().on("click", function () {
        getNetworkAnomaliesAndUpdateTable();
    });

// Delete network anomaly ----------------------------------------------------------------------------------------------

    const deleteNetworkAnomalyById = function (id) {
        console.log(`[INFO] Attempting to delete network anomaly with ID: ${id}`);
        $.ajax({
            type: "DELETE",
            url: `${networkAnomaliesURL}/${id}`,
            success: function (data, status, jqXHR) {
                console.info(`[SUCCESS] Deleted network anomaly with ID: ${id}`);
                console.debug("[DEBUG] Server response after delete:", data);
                getNetworkAnomalies();
                $("#modal_title").text("Delete Network Anomaly Success");
                $("#modal_contents").html(`<h5>Network anomaly with ID ${id} has been successfully deleted.</h5>`);
                $("#my_modal").modal("show");
            },
            error: function (jqXHR, status, error) {
                console.error(`[ERROR] Failed to delete network anomaly with ID: ${id}`, {
                    status: status,
                    error: error,
                    responseText: jqXHR.responseText
                });

                $("#modal_title").text("Delete Error");
                $("#modal_contents").html(`<h5>Failed to delete network anomaly. Error Status: ${jqXHR.status}</h5>`);
                $("#my_modal").modal("show");
            }
        });
    };

// Pie charts ----------------------------------------------------------------------------------------------------------

    let globalPieChart = null;
    const networkPieCharts = {};

    const createPieChart = function (chartElement, anomalyCount, nonAnomalyCount, title) {
        return new Chart(chartElement, {
            type: "pie",
            data: {
                labels: ["Anomalies", "Non-Anomalies"],
                datasets: [{
                    data: [anomalyCount, nonAnomalyCount],
                    backgroundColor: ["#FF3232", "#30B154"],
                    hoverBackgroundColor: ["#FF3232", "#30B154"]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: "top"
                    },
                    tooltip: {
                        callbacks: {
                            label: function (tooltipItem) {
                                return tooltipItem.label + ": " + tooltipItem.raw;
                            }
                        }
                    },
                    title: {
                        display: true,
                        text: title,
                        font: {
                            size: 23
                        },
                        padding: {
                            top: 10,
                            bottom: 30
                        }
                    }
                }
            }
        });
    };

    const updateGlobalPieChart = function (networkSummaries) {
        let anomaliesSum = 0;
        let nonAnomaliesSum = 0;

        networkSummaries.forEach(networkSummary => {
            anomaliesSum += networkSummary.anomalyCount || 0;
            nonAnomaliesSum += networkSummary.nonAnomalyCount || 0;
        });

        if (!globalPieChart) {
            const chartElement = document.getElementById("global-pie-chart");
            globalPieChart = createPieChart(chartElement, anomaliesSum, nonAnomaliesSum,
                "Anomalies vs Non-Anomalies");
            return;
        }

        globalPieChart.data.datasets[0].data = [anomaliesSum, nonAnomaliesSum];
        globalPieChart.update();
    };

    const updateNetworkPieCharts = function (networkSummaries) {
        const updatedChartIds = new Set();
        networkSummaries.forEach(networkSummary => {
            const chartId = `pie-chart-${networkSummary.networkId}`;
            updatedChartIds.add(chartId);

            if (!networkPieCharts[chartId]) {
                const canvas = $(
                    `<div class="col-md-6 col-sm-12"><canvas id="${chartId}" width="400" height="400"></canvas></div>`
                );
                $("#proportions .row").append(canvas);
                const chartElement = document.getElementById(chartId);
                networkPieCharts[chartId] = createPieChart(chartElement, networkSummary.anomalyCount,
                    networkSummary.nonAnomalyCount, `Network ${networkSummary.networkId}`);
            } else {
                const networkPieChart = networkPieCharts[chartId];
                networkPieChart.data.datasets[0].data = [networkSummary.anomalyCount, networkSummary.nonAnomalyCount];
                networkPieChart.options.plugins.title.text = `Network ${networkSummary.networkId}`;
                networkPieChart.update();
            }
        });

        const existingChartIds = Object.keys(networkPieCharts);
        existingChartIds.forEach(chartId => {
            if (!updatedChartIds.has(chartId)) {
                networkPieCharts[chartId].destroy();
                delete networkPieCharts[chartId];
                $(`#${chartId}`).parent().remove();
            }
        });
    };

// Line charts ---------------------------------------------------------------------------------------------------------

    let globalLineChart = null;
    const networkLineCharts = {};

    const createLineChart = function (chartElement, trafficVolumeSummary) {
        return new Chart(chartElement, {
            type: "line",
            data: {
                labels: trafficVolumeSummary.timestamps,
                datasets: [{
                    label: "Anomaly Volume",
                    data: trafficVolumeSummary.volumes,
                    borderColor: "#FF3232",
                    backgroundColor: "rgba(255, 50, 50, 0.5)",
                    fill: false,
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: "top"
                    },
                    title: {
                        display: true,
                        text: trafficVolumeSummary.title,
                        font: {
                            size: 23
                        },
                        padding: {
                            top: 10,
                            bottom: 30
                        }
                    }
                },
                scales: {
                    x: {
                        type: "time",
                        time: {
                            unit: "minute",
                            tooltipFormat: "yyyy-MM-dd HH:mm:ss"
                        },
                        title: {
                            display: true,
                            text: "Timestamp",
                            font: {
                                size: 20
                            }
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: "Traffic Volume",
                            font: {
                                size: 20
                            }
                        }
                    }
                }
            }
        });
    };

    const updateGlobalLineChart = function (networkAnomalies) {
        const timestamps = networkAnomalies.map(networkAnomaly => networkAnomaly.timestamp);
        const volumes = networkAnomalies.map(networkAnomaly => networkAnomaly.sizeInBytes);

        const trafficVolumeSummary = {
            title: "Global Network Anomalies",
            timestamps: timestamps,
            volumes: volumes
        };

        if (!globalLineChart) {
            const chartElement = document.getElementById("global-line-chart");
            globalLineChart = createLineChart(chartElement, trafficVolumeSummary);
            return;
        }

        globalLineChart.data.labels = trafficVolumeSummary.timestamps;
        globalLineChart.data.datasets[0].data = trafficVolumeSummary.volumes;
        globalLineChart.update();
    };

    const updateNetworkLineCharts = function (networkSummaries) {
        const updatedChartIds = new Set();
        networkSummaries.forEach(networkSummary => {
            const chartId = `line-chart-${networkSummary.networkId}`;
            updatedChartIds.add(chartId);

            if (!networkLineCharts[chartId]) {
                const canvas = $(
                    `<div class="col-md-6 col-sm-12"><canvas id="${chartId}" width="400" height="400"></canvas></div>`
                );
                $("#trends .row").append(canvas);
            }

            const chartElement = document.getElementById(chartId);

            console.info(`[INFO] Fetching network anomalies for network ID: ${networkSummary.networkId}`);
            $.ajax({
                type: "GET",
                url: `${networkAnomaliesURL}/network/${networkSummary.networkId}`,
                dataType: "json",
                success: function (data, status, jqXHR) {
                    console.info(`[SUCCESS] Fetched network anomalies for network ID: ${networkSummary.networkId}`);
                    console.debug("[DEBUG] Raw network anomalies data received:", data);

                    const timestamps = data.map(networkAnomaly => networkAnomaly.timestamp);
                    const volumes = data.map(networkAnomaly => networkAnomaly.sizeInBytes);

                    const trafficVolumeSummary = {
                        title: `Network ${networkSummary.networkId}`,
                        timestamps: timestamps,
                        volumes: volumes
                    };

                    if (!networkLineCharts[chartId]) {
                        console.log(`[INFO] Creating new line chart for chartId: ${chartId}`);
                        networkLineCharts[chartId] = createLineChart(chartElement, trafficVolumeSummary);
                        return;
                    }

                    console.log(`[INFO] Updating existing line chart for chartId: ${chartId}`);
                    const networkLineChart = networkLineCharts[chartId];
                    networkLineChart.data.labels = trafficVolumeSummary.timestamps;
                    networkLineChart.data.datasets[0].data = trafficVolumeSummary.volumes;
                    networkLineChart.options.plugins.title.text = trafficVolumeSummary.title;
                    networkLineChart.update();
                },
                error: function (jqXHR, status, error) {
                    console.error(`[ERROR] Failed to fetch anomalies for network ID: ${networkSummary.networkId}`, {
                        status: status,
                        error: error,
                        responseText: jqXHR.responseText
                    });
                }
            });
        });

        const existingChartIds = Object.keys(networkLineCharts);
        existingChartIds.forEach(chartId => {
            if (!updatedChartIds.has(chartId)) {
                networkLineCharts[chartId].destroy();
                delete networkLineCharts[chartId];
                $(`#${chartId}`).parent().remove();
            }
        });
    };

// Bar chart -----------------------------------------------------------------------------------------------------------

    let globalBarChart = null;

    const createBarChart = function (chartElement, networkAnomaliesSummary) {
        return new Chart(chartElement, {
            type: "bar",
            data: {
                labels: networkAnomaliesSummary.networkIds,
                datasets: [
                    {
                        label: "Anomaly Count",
                        data: networkAnomaliesSummary.anomalyCounts,
                        backgroundColor: "#FF3232",
                    },
                    {
                        label: "Non-Anomaly Count",
                        data: networkAnomaliesSummary.nonAnomalyCounts,
                        backgroundColor: "#30B154",
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: "top"
                    },
                    title: {
                        display: true,
                        text: "Anomaly and Non-Anomaly Counts per Network",
                        font: {
                            size: 23
                        },
                        padding: {
                            top: 10,
                            bottom: 30
                        }
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: "Network ID",
                            font: {
                                size: 20
                            }
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: "Count",
                            font: {
                                size: 20
                            }
                        },
                        beginAtZero: true
                    }
                }
            }
        });
    };

    const updateGlobalBarChart = function (networkSummaries) {
        const networkIds = networkSummaries.map(networkSummary => networkSummary.networkId);
        const anomalyCounts = networkSummaries.map(networkSummary => networkSummary.anomalyCount);
        const nonAnomalyCounts = networkSummaries.map(networkSummary => networkSummary.nonAnomalyCount);

        const networkAnomaliesSummary = {
            networkIds,
            anomalyCounts,
            nonAnomalyCounts
        };

        if (!globalBarChart) {
            const barChartElement = document.getElementById("global-bar-chart");
            globalBarChart = createBarChart(barChartElement, networkAnomaliesSummary);
            return;
        }

        globalBarChart.data.labels = networkAnomaliesSummary.networkIds;
        globalBarChart.data.datasets[0].data = networkAnomaliesSummary.anomalyCounts;
        globalBarChart.data.datasets[1].data = networkAnomaliesSummary.nonAnomalyCounts;
        globalBarChart.update();
    };

// Update charts -------------------------------------------------------------------------------------------------------

    const getNetworkSummariesAndUpdateCharts = function () {
        console.info("INFO: Fetching network summaries");
        $.ajax({
            type: "GET",
            url: `${networkSummariesURL}`,
            dataType: "json",
            success: function (data, status, jqXHR) {
                console.info(`INFO: Successfully fetched network summaries from: ${networkSummariesURL}`);
                console.debug("[DEBUG] Raw network summary data received:", data);

                updateGlobalPieChart(data);
                updateGlobalBarChart(data);
                updateNetworkPieCharts(data);
                updateNetworkLineCharts(data);
            },
            error: function (jqXHR, status, error) {
                console.error("ERROR: Failed to fetch network summaries.", {
                    status: status,
                    error: error,
                    responseText: jqXHR.responseText
                });

                $("#modal_title").text("GET Error");
                $("#modal_contents").html(`<h5>Error Status: ${jqXHR.status}</h5>`);
                $("#my_modal").modal("show");
            }
        });
    };

    getNetworkSummariesAndUpdateCharts();
    setInterval(getNetworkSummariesAndUpdateCharts, 2000);

    const getNetworkAnomaliesAndUpdateCharts = function () {
        console.info("INFO: Fetching network anomalies");
        $.ajax({
            type: "GET",
            url: `${networkAnomaliesURL}`,
            dataType: "json",
            success: function (data, status, jqXHR) {
                console.info(`INFO: Successfully fetched network anomalies from: ${networkAnomaliesURL}`);
                console.debug("[DEBUG] Raw network anomalies data received:", data);
                updateGlobalLineChart(data);
            },
            error: function (jqXHR, status, error) {
                console.error("ERROR: Failed to fetch network anomalies.", {
                    status: status,
                    error: error,
                    responseText: jqXHR.responseText
                });

                $("#modal_title").text("GET Error");
                $("#modal_contents").html(`<h5>Error Status: ${jqXHR.status}</h5>`);
                $("#my_modal").modal("show");
            }
        });
    }

    getNetworkAnomaliesAndUpdateCharts();
    setInterval(getNetworkAnomaliesAndUpdateCharts, 2000);
});

// main.js end ---------------------------------------------------------------------------------------------------------
