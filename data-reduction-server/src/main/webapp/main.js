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
    }
});
