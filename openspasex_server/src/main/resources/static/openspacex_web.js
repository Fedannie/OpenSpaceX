function getParams(params) {
    if (params == null) return "";
    return "?" + Object
        .keys(params)
        .map(function(key) {
            return key + "=" + encodeURIComponent(params[key])
        })
        .join("&")
}

function getRequest(url, callback, params) {
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function() {
        if (httpRequest.readyState === 4 && httpRequest.status === 200)
            callback(httpRequest.responseText);
    }
    httpRequest.open("GET", url + getParams(params), true);
    httpRequest.send( null );
}

function getYearParam(year) {
    if (year == null) return null;
    return {"year" : year};
}

function getNumberOfLaunches(year) {
    getRequest("http://localhost:8080/launches_count", response => {
        document.getElementById("launch_cnt").innerText = "There were " + response + " flights in total.";
    }, getYearParam(year))
}

function getSuccessRate(year = null) {
    getRequest("http://localhost:8080/success_rate", response => {
        document.getElementById("success_rate").innerText = "The success rate was " + (Number(response) * 100).toFixed(2) + "%.";
    }, getYearParam(year))
}

function getCost(year = null) {
    getRequest("http://localhost:8080/cost", response => {
        document.getElementById("cost").innerText = "All these launches cost " + currencyFormatter.format(response) + ".";
    }, getYearParam(year))
}

function getCrewSize(year = null) {
    getRequest("http://localhost:8080/crew", response => {
        document.getElementById("crew_size").innerText = " The total number of " + response + " astronauts participated in these flights.";
    }, getYearParam(year))
}

function getTakeoffWeight(year = null) {
    getRequest("http://localhost:8080/mass", response => {
        document.getElementById("weight").innerText = "The total load of the spaceships was " + numberFormat.format(response) + " kg.";
    }, getYearParam(year))
}

let currentYear = new Date().getFullYear()

function reload() {
    let select = document.getElementById("year_select");
    let year = null;
    if (select.options.length > 0) {
        year = select.options[select.selectedIndex].value;
        year = (year === "All years") ? null : parseInt(year)
    }

    getNumberOfLaunches(year);
    getSuccessRate(year);
    getCost(year);
    getCrewSize(year);
    getTakeoffWeight(year);
}

function onPageLoad() {
    let select = document.getElementById("year_select");
    for(let i = currentYear; i >= 2006; i--) {
        let option = document.createElement('option');
        option.text = option.value = String(i);
        select.add(option, 0);
    }
    let allYearsOption = document.createElement('option');
    allYearsOption.text = "All years"
    select.add(allYearsOption, 0);
    select.selectedIndex = 0;

    reload()
}

var currencyFormatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0
});
var numberFormat = new Intl.NumberFormat('en-IN');