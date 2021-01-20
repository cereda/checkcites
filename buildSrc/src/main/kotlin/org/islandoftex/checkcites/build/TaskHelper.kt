// SPDX-License-Identifier: BSD-3-Clause
package org.islandoftex.checkcites.build

import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.zeroturnaround.exec.ProcessExecutor

object TaskHelper {
    /**
     * Checks whether the commands are available in the system path.
     * @param commands Array of commands to be checked.
     * @throws IOException The command does not exist.
     */
    @Throws(IOException::class)
    fun assertAvailability(vararg commands: String) {
        try {
            @Suppress("SpreadOperator")
            execute(File("."), *commands)
        } catch (_: IOException) {
            throw IOException("The command has returned an invalid " +
                    "exit value. Chances are the command is not " +
                    "available in the system path. Make sure the " +
                    "command exists and try again. The application " +
                    "will halt now.")
        }
    }

    /**
     * Executes the command and arguments in the provided directory.
     * @param directory The working directory.
     * @param call The proper call with the command and arguments.
     * @throws IOException An error has occurred during the execution.
     */
    @Throws(IOException::class)
    fun execute(directory: File, vararg call: String) {
        try {
            ProcessExecutor().command(call.toList()).directory(directory)
                    .exitValueNormal().execute()
        } catch (_: Exception) {
            throw IOException("The command call has returned an " +
                    "invalid exit value. Chances are the arguments are " +
                    "incorrect. Make sure the call contains valid arguments " +
                    "and try again.")
        }
    }

    /**
     * Creates a shell script file for checkcites.
     * @param file The file reference.
     * @throws IOException The file could not be written.
     */
    @Throws(IOException::class)
    fun createScript(file: File) {
        try {
            file.writeText("""
                #!/bin/sh
                # Public domain. Originally written by Norbert Preining and Karl Berry, 2018.
                # Note from Paulo: this script provides better Cygwin support than our original
                # approach, so the team decided to use it as a proper wrapper for checkcites as well.

                scriptname=`basename "$0"`
                jar="${'$'}scriptname.jar"
                jarpath=`kpsewhich --progname="${'$'}scriptname" --format=texmfscripts "${'$'}jar"`

                kernel=`uname -s 2>/dev/null`
                if echo "${'$'}kernel" | grep CYGWIN >/dev/null; then
                  CYGWIN_ROOT=`cygpath -w /`
                  export CYGWIN_ROOT
                  jarpath=`cygpath -w "${'$'}jarpath"`
                fi

                exec java -jar "${'$'}jarpath" "${'$'}@"
            """.trimIndent() + "\n")
        } catch (_: IOException) {
            throw IOException("I could not create the shell script for " +
                    "checkcites due to an IO error. Please make sure the " +
                    "current directory has the correct permissions " +
                    "and try again. The application will halt now.")
        }
    }

    /**
     * Creates a man page file for checkcites.
     * @param file The file reference.
     * @param version The tool version.
     * @throws IOException The file could not be written.
     */
    @Throws(IOException::class)
    fun createManPage(file: File, version: String) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        try {
            file.writeText("""
                TODO write manpage for checkcites
            """.trimIndent() + "\n")
        } catch (_: IOException) {
            throw IOException("I could not create the man page for " +
                    "checkcites due to an IO error. Please make sure" +
                    "the current directory has the correct permissions " +
                    "and try again. The application will halt now.")
        }
    }
}
