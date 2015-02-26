require(["jquery", "barChart"], function ($, BarChart) {

    var container = $("#container")[0];
    var barChart = BarChart(container);

    // Set axis properties.
    barChart.set({
        yAttribute: "count",
        yAxisLabel: "Count",
        yAxisTickFormat: "",
        margin: {
          top: 20,
          right: 20,
          bottom: 50,
          left: 100
        }
    });

    function reduceData(dimension, callback){
        // Invoke the API for computing data cubes with Spark.
        $.ajax({
            type: "GET",
            url: "/reduceData",
            data: {
                options: JSON.stringify({
                    dataset: "adult",
                    cube: {
                        dimensions: [
                            { name: dimension }
                        ],
                        measures: [
                            { aggregationOp: "count" }
                        ]
                    }
                })
            },
            success: function(data){
                callback(JSON.parse(data));
            }

                // Example output:
                // [
                //   { "sex": "Female", "race": "White", "count": 8642, "capital-gain": 4957141 },
                //   { "sex": "Female", "race": "Other", "count": 109, "capital-gain": 27759 },
                //   { "sex": "Male", "race": "Black", "count": 1569, "capital-gain": 1102151 },
                //   { "sex": "Female", "race": "Asian-Pac-Islander", "count": 346, "capital-gain": 269339 },
                //   { "sex": "Female", "race": "Amer-Indian-Eskimo", "count": 119, "capital-gain": 64808 },
                //   { "sex": "Male", "race": "White", "count": 19174, "capital-gain": 26242964 },
                //   { "sex": "Male", "race": "Other", "count": 162, "capital-gain": 225534 },
                //   { "sex": "Female", "race": "Black", "count": 1555, "capital-gain": 803303 },
                //   { "sex": "Male", "race": "Asian-Pac-Islander", "count": 693, "capital-gain": 1266675 },
                //   { "sex": "Male", "race": "Amer-Indian-Eskimo", "count": 192, "capital-gain": 129650 }
                // ]
        });
    }

    $("#nominalColumns").change(function (a){
        var selectedOption = $("#nominalColumns option:selected")[0];
        var selectedColumn = selectedOption.value;
        var selectedColumnLabel = selectedOption.text;

        reduceData(selectedColumn, function(data){
            data = _.sortBy(data, "count").reverse();
            barChart.data = data;
            barChart.xAttribute = selectedColumn;
            barChart.xAxisLabel = selectedColumnLabel;
        });
    }).change();
    
    // Set the bar chart size
    // based on the size of its container,
    function computeBox(){
        barChart.box = {
            width: container.clientWidth,
            height: container.clientHeight
        };
        console.log(barChart.box);
    }

    // once to initialize `model.box`, and
    computeBox();

    // whenever the browser window resizes in the future.
    window.addEventListener("resize", computeBox);
});
