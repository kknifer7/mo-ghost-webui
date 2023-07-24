const FILE_STORAGE_PATH_EXP = /(^\/)([\w]*\/)*\w+$/;

const pathUtils = {
  isValidStoragePath: (pathStr: string) => FILE_STORAGE_PATH_EXP.test(pathStr),
};

export default pathUtils;
