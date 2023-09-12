package matrixLib;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MatrixStringParser {

    public static Matrix parse(List<String> mtxFileLines) throws MatrixException {
        Matrix newMtx = new Matrix();
        if (mtxFileLines.size() != 2) throw new MatrixException("Wrong matrix file format");
        String rowsColsLine = mtxFileLines.get(0);
        String contentsLine = mtxFileLines.get(1);

        String[] preParsedData;
        if (isRightRowsColsLine(rowsColsLine) && isRightContentsLine(contentsLine)) {
            preParsedData = rowsColsLine.split("\\s");
            newMtx.changeRowsAndColumns(Integer.parseInt(preParsedData[0]),
                    Integer.parseInt(preParsedData[1]));

            preParsedData = contentsLine.split("\\s");
            double[][] newMtxContents = new double[newMtx.getRows()][newMtx.getColumns()];
            int counter = 0;
            for (int i = 0; i < newMtx.getRows(); i++) {
                for (int j = 0; j < newMtx.getColumns(); j++) {
                    if (counter < preParsedData.length) {
                        newMtxContents[i][j] = Double.parseDouble(preParsedData[counter]);
                        counter++;
                    }
                }
            }
            newMtx.setContents(newMtxContents);
        } else throw new MatrixException("Wrong matrix file format");
        return newMtx;
    }

    private static boolean isRightContentsLine(String contentsLine) {
        Pattern pattern = Pattern.compile("^(\\d*\\.\\d+\\s)*(\\d*\\.\\d+)$");
        Matcher matcher = pattern.matcher(contentsLine);
        return matcher.matches();
    }

    private static boolean isRightRowsColsLine(String rowsColsLine) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*\\s[1-9]\\d*$");
        Matcher matcher = pattern.matcher(rowsColsLine);
        return matcher.matches();
    }
}
