package ma.pam.ajitsowak.woolib.models


class SettingOption {

    lateinit var id: String
    lateinit var label: String
    lateinit var description: String
    lateinit var value: String

    lateinit var default_value: String
    lateinit var tip: String
    lateinit var placeholder: String
    lateinit var type: String
    lateinit var options: Map<String, String>
    lateinit var group_id: String
}
