package br.com.fabriciocurvello.appcamerajavaandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private String currentPhotoPath;

    private ImageView imgFoto;


    // Definindo o ActivityResultLauncher para capturar fotos
    private final ActivityResultLauncher<Intent> capturarFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == RESULT_OK) {
                    // Corrigir a rotação da imagem e exibi-la
                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    Bitmap rotatedBitmap = rotacionarImagem(bitmap, currentPhotoPath);
                    imgFoto.setImageBitmap(rotatedBitmap);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgFoto = findViewById(R.id.img_foto);

        // Ao clicar na ImageView, a câmera é aberta
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

                    // Se a permissão de câmera ou de gravação não foi concedida, solicite-as
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    }, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    // Se as permissões já foram concedidas, abra a câmera
                    abrirCamera();
                }
            }
        });
    } // fim do onCreate()

    private void abrirCamera() {
        Intent obterFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Verifica se há uma atividade de câmera disponível
        if (obterFotoIntent.resolveActivity(getPackageManager()) != null) {
            // Cria um arquivo temporário para armazenar a imagem
            File photoFile = null;
            try {
                photoFile = criarArquivoDeImagem();
            } catch (Exception ex) {
                Toast.makeText(this, "Erro ao criar o arquivo de imagem", Toast.LENGTH_SHORT).show();
                return; // Sai do método se não conseguir criar o arquivo
            }
            // Prossegue se o arquivo foi criado com sucesso
            if (photoFile != null) {
                // Aqui está a chamada ao FileProvider com a URI correta
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.com.fabriciocurvello.appcamerajavaandroid.fileprovider",  // Use o nome correto do pacote
                        photoFile);


                obterFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                capturarFoto.launch(obterFotoIntent);
            }
        }
    }

    // Criar arquivo temporário para salvar a foto
    private File criarArquivoDeImagem() throws IOException{
        // Cria um nome de arquivo único baseado na data
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // Nome do arquivo
                ".jpg",         // Extensão
                storageDir      // Diretório de armazenamento
        );

        // Salva o caminho do arquivo para uso posterior
        currentPhotoPath = image.getAbsolutePath();
        return image;

    }

    // Tratar a resposta da solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, abrir câmera
                abrirCamera();
            } else {
                // Permissão negada
                Toast.makeText(this, "PERMISSÃO DE CÂMERA NEGADA!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Exibindo a imagem capturada
    private Bitmap rotacionarImagem(Bitmap img, String photoPath) {

        ExifInterface exif; // ExifInterface captura os metadados da foto. É possível saber a orientação por ele.
        try {
            exif = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
            return img;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix(); // Matriz permite rotacionar a foto.
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return img;
        }
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }
}