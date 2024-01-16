package sale.clear.documentoscopysampleandroidkotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.clear.studio.csdocs.entries.CSDocumentoscopy
import com.clear.studio.csdocs.entries.CSDocumentoscopySDK
import com.clear.studio.csdocs.entries.CSDocumentoscopySDKError
import com.clear.studio.csdocs.entries.CSDocumentoscopySDKListener
import com.clear.studio.csdocs.entries.CSDocumentoscopySDKResult
import sale.clear.documentoscopysampleandroidkotlin.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val TAG = "DocsSDK"

        //Preencha as variáveis abaixo:
        var clientId =  ""
        var clientSecret = ""
        var identifierId = ""
        //cpf Opcional
        var cpf = ""

        binding.clientId.setText(clientId)
        binding.clientSecret.setText(clientSecret)
        binding.identifierId.setText(identifierId)
        binding.cpf.setText(cpf)

        binding.startSdk.setOnClickListener {
            binding.sessionId.text = ""
            clientId = binding.clientId.text.toString()
            clientSecret = binding.clientSecret.text.toString()
            identifierId = binding.identifierId.text.toString()
            cpf = binding.cpf.text.toString()

            if (clientId.isEmpty() || clientSecret.isEmpty() || identifierId.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CSDocumentoscopySDK.initialize(application, CSDocumentoscopy(clientID = clientId, clientSecret = clientSecret, cpf = cpf, identifierId = identifierId), object:
                CSDocumentoscopySDKListener {
                override fun didOpen(){
                    Log.d(TAG, "SDK Foi Aberto")
                }
                override fun didTapClose(){
                    Log.d(TAG, "Usuário encerrou manualmente o fluxo")
                }
                override fun didReceiveError(error : CSDocumentoscopySDKError){
                    val errorText = "${error.text} - errorCode: ${error.errorCode}"
                    binding.sessionId.setText(errorText)
                    Log.d(TAG, "Error: $errorText")
                }
                override fun didFinishCapture(result: CSDocumentoscopySDKResult){
                    val sessionIdText = "Session Id: " + result.sessionId
                    binding.sessionId.text = sessionIdText
                    binding.csDocumentoscopySDKDocumentTypeView.text = "CSDocumentoscopySDKDocumentType: ${result.documentType.toString()}"
                }
            })
        }
    }
}