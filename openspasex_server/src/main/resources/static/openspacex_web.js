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

let currentYear = new Date().getFullYear()
var remainsToLoad = 0;

function getYearParam(year) {
    if (year == null) return null;
    return {"year" : year};
}

function getNumberOfLaunches(year) {
    remainsToLoad++;
    getRequest("http://localhost:8080/launches_count", response => {
        document.getElementById("launch_cnt").innerText =
            "There were " + String(response) + " flights in total.";
        onLoaded();
    }, getYearParam(year))
}

function getSuccessRate(year = null) {
    remainsToLoad++;
    getRequest("http://localhost:8080/success_rate", response => {
        document.getElementById("success_rate").innerText =
            "The success rate was " + (Number(response) * 100).toFixed(2) + "%.";
        onLoaded();
    }, getYearParam(year))
}

function getCost(year = null) {
    remainsToLoad++;
    getRequest("http://localhost:8080/cost", response => {
        document.getElementById("cost").innerText =
            "All these launches cost " + currencyFormatter.format(response) + ".";
        onLoaded();
    }, getYearParam(year))
}

function getCrewSize(year = null) {
    remainsToLoad++;
    getRequest("http://localhost:8080/crew", response => {
        document.getElementById("crew_size").innerText =
            "The total number of " + String(response) + " astronauts participated in these flights.";
        onLoaded();
    }, getYearParam(year))
}

function getTakeoffWeight(year = null) {
    remainsToLoad++;
    getRequest("http://localhost:8080/mass", response => {
        document.getElementById("weight").innerText =
            "The total load of the spaceships was " + numberFormat.format(response) + " kg.";
        onLoaded();
    }, getYearParam(year))
}

function reload() {
    toggleLoading(true);
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

function addRocket(rocket) {
    const divider = document.createElement("hr");
    divider.className = "solid";
    document.getElementById("rockets").appendChild(divider);

    const line = document.createElement("div");
    line.className = "rocket";

    let image = new Image();
    image.src = rocket.image;
    image.style.margin = "1% 1% 0% 0%";
    image.style.width = "20%";
    image.style.display = "block-inline";

    line.appendChild(image);

    const summary = document.createElement("div");
    summary.className = "description";
    summary.style.position = "absolute";
    summary.style.left = "23%";


    const name = document.createElement("h3");
    name.innerText = rocket.name;
    summary.appendChild(name);

    const cost = document.createElement("p");
    cost.innerText = "Cost per launch: " + currencyFormatter.format(String(rocket.cost_per_launch));
    summary.appendChild(cost);

    const description = document.createElement("p");
    description.innerText = rocket.description;
    summary.appendChild(description);

    line.appendChild(summary);

    document.getElementById("rockets").appendChild(line);
}

function loadRockets() {
    getRequest("http://localhost:8080/rockets", response => {
        const rockets = JSON.parse(response);

        for (let rocket of rockets) addRocket(rocket);

        document.getElementById("rockets").style.display = "block";
    });
}

function loadNextLaunch() {
    getRequest("http://localhost:8080/next_launch", response => {
        const launch = JSON.parse(response);
        document.getElementById("name").innerText = launch.name;
        document.getElementById("rocket").innerText = "Rocket: " + launch.rocket.name;
        document.getElementById("date").innerText =
            new Intl.DateTimeFormat('en-GB', { dateStyle: 'full', timeStyle: 'long' })
                .format(new Date(launch.date_utc));
        document.getElementById("launch_logo").src = launch.logo;
        document.getElementById("launch_logo").width = 100;
        document.getElementById("launch_logo").height = 100;
        document.getElementById("next_launch").style.display = "inline";
    });
}

function onLoaded() {
    remainsToLoad--;
    if (remainsToLoad > 0) return;
    toggleLoading(false);
}

function toggleLoading(on) {
    document.getElementById("loader").style.display = on ? "inline-block" : "none";
    document.getElementById("fact_list").style.display = on ? "none" : "block";
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

    loadRockets();
    loadNextLaunch();
    reload()
}

var currencyFormatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0
});
var numberFormat = new Intl.NumberFormat('en-IN');