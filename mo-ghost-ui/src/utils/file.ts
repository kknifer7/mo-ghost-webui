const fileUtils = {
  exportFile: function (
    fileName: string,
    data: BlobPart,
    type = 'application/vnd.ms-excel'
  ) {
    if (!data) return;

    const url = window.URL.createObjectURL(new Blob([data], { type }));
    const link = document.createElement('a');

    link.style.display = 'none';
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link); //下载完成移除元素
    window.URL.revokeObjectURL(url); //释放掉blob对象
  },
};

export default fileUtils;
