package mx.gob.imss.cit.pmc.cierreanualimss.utils;

import org.springframework.batch.core.ExitStatus;

public class ExitStatusCierreAnualIMSS extends ExitStatus {
	private static final long serialVersionUID = 973460612243334499L;

	public ExitStatusCierreAnualIMSS(String exitCode) {
        super(exitCode);
    }

    public static final ExitStatusCierreAnualIMSS GENERATION_FILE_FAILED = new ExitStatusCierreAnualIMSS("generation_file_failed");

    public static final ExitStatusCierreAnualIMSS UPLOAD_SFTP1_FAILED = new ExitStatusCierreAnualIMSS("upload_sftp1_failed");

    public static final ExitStatusCierreAnualIMSS UPLOAD_SFTP2_FAILED = new ExitStatusCierreAnualIMSS("upload_sftp2_failed");

}
