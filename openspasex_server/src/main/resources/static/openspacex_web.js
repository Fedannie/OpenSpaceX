function getNumberOfLaunches(year = null) {
    return 1;
}

function getSuccessRate(year = null) {
    return 1;
}

function getCost(year = null) {
    return 1;
}

function getCrewSize(year = null) {
    return 1;
}

function getTakeoffWeight(year = null) {
    return 1;
}

let currentYear = new Date().getFullYear()

function reload() {
    let select = document.getElementById("year_select");
    let year = null;
    if (select.options.length > 0) {
        year = select.options[select.selectedIndex].value;
        year = (year === "All years") ? null : parseInt(year)
    }

    document.getElementById("launch_cnt").innerText = "There were " + getNumberOfLaunches(year) + " flights in total.";
    document.getElementById("success_rate").innerText = "The success rate was " + getSuccessRate(year) + ".";
    document.getElementById("cost").innerText = "All these launches costed $" + getCost(year) + ".";
    document.getElementById("crew_size").innerText = " The total number of " + getCrewSize(year) + " participated in these flights.";
    document.getElementById("weight").innerText = "The total load of the spaceships was " + getTakeoffWeight(year) + ".";
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