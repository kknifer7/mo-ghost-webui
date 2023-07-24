function loadJs(src: string) {
  return new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = src;
    document.body.appendChild(script);

    script.onload = () => {
      resolve(0);
    };
    script.onerror = () => {
      reject();
    };
  });
}

export default loadJs;
