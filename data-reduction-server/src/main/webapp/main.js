// Invoke the API for computing data cubes with Spark.
$.ajax({
    type: "GET",
    url: "/reduceData",
    data: {
        options: JSON.stringify({
            dataset: "adult",
            cube: {
                dimensions: [
                    { name: "sex" },
                    { name: "race" }
                ],
                measures: [
                    { aggregationOp: "count" },
                    { aggregationOp: "sum", name: "capital-gain" }
                ]
            }
        })
    },
    success: function(data){

        // Pretty-print the returned JSON.
        console.log(JSON.stringify(JSON.parse(data), null, 2));

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
    }
});
