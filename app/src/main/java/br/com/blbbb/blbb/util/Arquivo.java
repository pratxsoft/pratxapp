package br.com.blbbb.blbb.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class Arquivo {

	private final String CAMINHO_EXPORT = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "BLSoft"
			+ File.separator + "Export";
	private final String CAMINHO_RELATORIO = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "BLSoft"
			+ File.separator + "Relatorio" + File.separator;
	private final String CAMINHO_DESTINO_BANCO = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "BLSoft"
			+ File.separator + "Backup" + File.separator + "BLVendasBKP.db";

	/**
	 * escreve o arquivo
	 * 
	 * @param pContext
	 * @param pNomeDoArquivo
	 *            (com extensao)
	 * @param pTexto
	 * @return boolean
	 */
	public boolean escreverArquivo(String pNomeDoArquivo, String pTexto) {
		FileOutputStream fos = null;

		// verifico se o cartao sd esta montado
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {
				final File dir = new File(this.CAMINHO_EXPORT);

				if (!dir.exists()) {
					dir.mkdirs();
				}

				final File myFile = new File(dir, pNomeDoArquivo);

				if (!myFile.exists()) {
					myFile.createNewFile();
				}

				fos = new FileOutputStream(myFile);

				fos.write(pTexto.getBytes());
				fos.close();
			} catch (IOException e) {
				Log.d("ERRO", e.getMessage());
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	public String getCaminhoRelatorio() {

		final File dir = new File(this.CAMINHO_RELATORIO);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		return this.CAMINHO_RELATORIO;
	}

	// importing database
	public void importDB(Context pContext) {

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = pContext.getDatabasePath("BLVendasDB");

			if (sd.canWrite()) {
				String backupDBPath = File.separator + "BLSoft"
						+ File.separator + "BLVendasDB";

				File backupDB = new File(data, "");
				File currentDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(pContext, "Banco de dados foi restaurado!",
						Toast.LENGTH_LONG).show();

			}
		} catch (Exception e) {
			Toast.makeText(pContext, e.toString(), Toast.LENGTH_LONG).show();

		}
	}

	// exporting database
	public void exportDB(Context pContext) {
		// TODO Auto-generated method stub

		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = pContext.getDatabasePath("BLVendasDB");

			if (sd.canWrite()) {
				String backupDBPath = File.separator + "BLSoft"
						+ File.separator + "BLVendasDB";

				File currentDB = new File(data, "");
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(pContext, "Backup realizado com sucesso!",
						Toast.LENGTH_LONG).show();

			}
		} catch (FileNotFoundException e) {
			Log.d("Arquivo", e.getMessage());
			Toast.makeText(
					pContext,
					"Banco de dados nao existe ou nao foi encontrado um local apropriado para criacao do Backup",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(pContext, "Ocorreu um erro na geracao do Backup",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Cria o diretorio da aplicacao no cartao externo
	 * 
	 * @param pContext
	 */
	public void criarDiretorioExterno(Context pContext) {

		File sd = Environment.getExternalStorageDirectory();

		if (sd.canWrite()) {

			//diretorio raiz
			final File diretorio = new File(sd, File.separator + "BLSoft");

			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			
			//diretorio de arquivos
			final File arquivo = new File(sd, File.separator + "BLSoft" + File.separator + "Arquivos");

			if (!arquivo.exists()) {
				arquivo.mkdirs();
			}
			
			//diretorio de Exportacoes
			final File export = new File(sd, File.separator + "BLSoft" + File.separator + "Export");

			if (!export.exists()) {
				export.mkdirs();
			}
			
			//diretorio de Importacoes
			final File importe = new File(sd, File.separator + "BLSoft" + File.separator + "Import");

			if (!importe.exists()) {
				importe.mkdirs();
			}

		}
	}

	public boolean salvarImagem(ImageView imageView) {
		Drawable drawable = imageView.getDrawable();
		Rect bounds = drawable.getBounds();
		Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.draw(canvas);
		FileOutputStream fOut = null;

		File sd = Environment.getExternalStorageDirectory();

		if (sd.canWrite()) {
			String diretorioLogo = File.separator + "BLSoft" + File.separator
					+ "Arquivos";

			final File dir = new File(sd, diretorioLogo);

			if (!dir.exists()) {
				dir.mkdirs();
			}

			File destinoLogo = new File(sd, diretorioLogo + File.separator
					+ "Logo.PNG");

			try {

				fOut = new FileOutputStream(destinoLogo);
				bitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fOut != null) {
					try {
						fOut.close();
					} catch (IOException e) {
						// report error
					}
				}

			}
		}
		return false;
	}

	public Bitmap carregarImagemExterna(String pSubCaminho, String pNomeImagem) {
		
		//exemplo de chamada
		//"carregarImagemExterna(/Aqeel/Images/","Image2.PNG");
		
		File imageFile = new File(Environment.getExternalStorageDirectory().toString() + pSubCaminho + pNomeImagem);
		Bitmap myBitmap = null;
		if (imageFile.exists()) {

			myBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + pSubCaminho + pNomeImagem);
			// myImage = (ImageView) findViewById(R.id.savedImage);
			// myImage.setImageBitmap(myBitmap);
		}
		return myBitmap;
	}

}
