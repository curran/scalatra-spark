$.ajax({
    type: "GET",
    url: "/reduceData",
    data: {
        options: JSON.stringify({
            dataset: "adult",
            sample: {
                n: 5
            },
            filter: [
                { name: "foo" },
                { name: "bar" }
            ],
            aggregate: {
                dimensions: [
                    { name: "baz" },
                    { name: "bat" }
                ],
                measures: [
                    { aggregationOp: "count" },
                    { aggregationOp: "sum", name: "bag" }
                ]
            }
        })
    },
    success: function(data){
        console.log(data);
        //console.log(JSON.stringify(JSON.parse(data), null, 2));
    }
});
