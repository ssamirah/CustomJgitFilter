package com.general.jgit.custom.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.jgit.attributes.FilterCommand;
import org.eclipse.jgit.attributes.FilterCommandFactory;
import org.eclipse.jgit.attributes.FilterCommandRegistry;
import org.eclipse.jgit.lib.Repository;

/**
 * The Class CustomJgitCleanFilter.
 *
 */
public class CustomJgitCleanFilter extends FilterCommand {

	/** The Constant FACTORY. */
	public static final FilterCommandFactory FACTORY = CustomJgitCleanFilter::new;


	/**
	 * Register.
	 */
	static void register() {
		FilterCommandRegistry.register("jgit://filter/purge/" + org.eclipse.jgit.lib.Constants.ATTR_FILTER_TYPE_CLEAN,
				FACTORY);
	}

	/**
	 * Instantiates a new scg jgit pre process filter.
	 * @param db the db
	 * @param in the in
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public CustomJgitCleanFilter(Repository db, InputStream in, OutputStream out) throws IOException {
		super(in, out);
	}

	/**
	 * Run.
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public int run() throws IOException {
		int bytesRead;
		int bigBufferSize = 8192;
		byte[] bigBuffer = new byte[bigBufferSize];
		
			while ((bytesRead = this.in.read(bigBuffer)) != -1) {
				if (bytesRead > 0) {
					this.out.write(bigBuffer, 0, bytesRead);
				}
			}

	
		
		return bytesRead;

	}

}
