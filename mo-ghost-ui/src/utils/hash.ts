import { Hasher, md5 } from 'js-md5';
import type { TreeNode } from './models';
import { QTree, QTreeNode } from 'quasar';

// 20MB
const MinHashCalcSize = 1024 * 1024 * 20;
// 200MB
const MaxHashCalcSize = 1024 * 1024 * 200;
// 1GB
const SmallHashCalcSize = 1024 * 1024 * 1000;
// 2GB
const MediumHashCalcSize = 1024 * 1024 * 2000;
// 5GB
const LargeHashCalcSize = 1024 * 1024 * 5000;
// 10GB
const XLargeHashCalcSize = 1024 * 1024 * 10000;

/**
 * 分片跳跃计算哈希值（大文件时耗时较长）
 * @param file 文件
 * @returns 哈希值
 */
function calcHash(file: File) {
  const hash = md5.create();
  const fileSize = file.size;
  let chunkSize;
  let jumpSize;
  let jumpCount;

  /**
   * 此哈希计算方式暂定，后续可能会调整
   */
  if (fileSize <= SmallHashCalcSize) {
    chunkSize = fileSize;
    jumpSize = fileSize;
    jumpCount = 0;
  } else if (fileSize <= MediumHashCalcSize) {
    chunkSize = MinHashCalcSize;
    jumpCount = Math.ceil(fileSize / (20 * chunkSize));
    jumpSize = Math.floor(fileSize / (2 * jumpCount));
  } else if (fileSize <= LargeHashCalcSize) {
    chunkSize = MinHashCalcSize;
    jumpCount = Math.ceil(fileSize / (50 * chunkSize));
    jumpSize = Math.floor(fileSize / (2 * jumpCount));
  } else if (fileSize <= XLargeHashCalcSize) {
    chunkSize = MinHashCalcSize;
    jumpCount = Math.ceil(fileSize / (100 * chunkSize));
    jumpSize = Math.floor(fileSize / (2 * jumpCount));
  } else {
    chunkSize = MinHashCalcSize;
    jumpCount = Math.ceil(fileSize / (500 * chunkSize));
    jumpSize = Math.floor(fileSize / (2 * jumpCount));
  }
  jumpSize = Math.min(jumpSize, MaxHashCalcSize);

  for (
    let offset = 0;
    offset < fileSize;
    offset += jumpSize * (jumpCount + 1)
  ) {
    const chunk = file.slice(offset, offset + jumpSize);
    const arrayBuffer = new ArrayBuffer(chunk.size);
    const view = new Uint8Array(arrayBuffer);

    hash.update(view);
  }

  return hash.hex();
}

/**
 * 快速计算哈希值（从头尾取固定大小文件内容 + 文件名称、文件大小）
 * @param file 文件
 * @return 哈希值
 */
function fastCalcHash(file: File) {
  const hash = md5.create();
  const fileSize = file.size;

  console.time();
  if (fileSize <= MaxHashCalcSize) {
    for (let offset = 0; offset < fileSize; offset += MinHashCalcSize) {
      updateHasher(hash, file, offset, offset + MinHashCalcSize);
    }
  } else {
    updateHasher(hash, file, 0, MinHashCalcSize);
    updateHasher(hash, file, fileSize - MinHashCalcSize, fileSize);
    hash.update(file.name);
    hash.update(fileSize.toString());
  }
  console.timeEnd();

  return hash.hex();
}

function updateHasher(hasher: Hasher, file: File, start: number, end: number) {
  const chunk = file.slice(start, end);
  const arrayBuffer = new ArrayBuffer(chunk.size);
  const view = new Uint8Array(arrayBuffer);

  hasher.update(view);
}

function fillFileTree(tree: QTree, fileList: FileList) {
  let file: File | null;
  let fileNameArray: Array<string>;
  let fileNameArrayLen: number;
  let key: string;
  const nodes = tree.nodes;

  for (let i = 0; i < fileList.length; i++) {
    file = fileList.item(i);
    if (!file) continue;
    key = file.webkitRelativePath;
    fileNameArray = key.split('/');
    fileNameArrayLen = fileNameArray.length;
    fileNameArray.forEach((name: string, idx: number) => {
      let key = '';
      for (let i = 0; i < idx; i++) {
        key += fileNameArray[i];
      }

      const node = tree.getNodeByKey(key);
      // TODO 将这个方法迁移到FileUploader后，构建树结构对象
      if (node) {
        if (node.children) {
          // 压入文件结点
          node.children.push({});
        } else {
          // 压入文件结点
          node.children = [{}];
        }
      } else {
        // 压入目录结点
        nodes.push({});
      }
    });
    console.log(file.webkitRelativePath.split('/'));
  }
}

export default {
  calcHash,
  fastCalcHash,
  fillFileTree,
};
