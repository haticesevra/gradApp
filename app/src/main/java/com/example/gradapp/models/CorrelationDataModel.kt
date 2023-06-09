package com.example.gradapp.models

class CorrelationDataModel {
    var status: String = ""
    var data: List<CorrData> = listOf()

    class CorrData {
        var _id: String = ""
        var DNS : String = ""
        var MacAddress: String = ""
        var Ip: String = ""
        var EtiValue: String = ""
        var EtiType: String = ""
        var AlertName: String = ""
        var CreationDate: String = ""
        var MonitorUUID: String = ""
        var Subcategory: String = ""
        var Category: String = ""
        var NodeID: String = ""
        var Node: String = ""
        var Source: String = ""
        var Status: String = ""
        var Severity: String = ""
        var EventID: String = ""
    }
}
