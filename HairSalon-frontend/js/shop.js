window.map = null;

main();
async function main() {
  const LOCATION = { center: [27.610120723557344, 53.884848342819005], zoom: 13 };
  const LOCATION1 = { center: [27.63001000667869, 53.89026017551443], zoom: 15 };
  const LOCATION2 = { center: [27.590916591152947, 53.88133234553783], zoom: 15 };
  const LOCATION3 = { center: [27.58608796178701, 53.89216891552366], zoom: 15 };
  const LOCATION4 = { center: [27.63903423212963, 53.8772552903979], zoom: 15 };

  await ymaps3.ready;
  const { YMap, YMapDefaultSchemeLayer, YMapControls, YMapDefaultFeaturesLayer, YMapMarker } = ymaps3;

  const { YMapZoomControl } = await ymaps3.import('@yandex/ymaps3-controls@0.0.1');

  map = new YMap(document.getElementById('map'), { location: LOCATION });

  map.addChild((scheme = new YMapDefaultSchemeLayer()));
  map.addChild(new YMapDefaultFeaturesLayer());

  map.addChild(new YMapControls({ position: 'right' }).addChild(new YMapZoomControl({})));

  const el1 = document.createElement('img');
  el1.className = 'my-marker';
  el1.src = '../svg/pin.svg';
  el1.onclick = () => map.update({ location: { ...LOCATION1, duration: 400 } });
  map.addChild(new YMapMarker({ coordinates: LOCATION1.center }, el1));

  const el2 = document.createElement('img');
  el2.className = 'my-marker';
  el2.src = '../svg/pin.svg';
  el2.onclick = () => map.update({ location: { ...LOCATION2, duration: 400 } });
  map.addChild(new YMapMarker({ coordinates: LOCATION2.center }, el2));

  const el3 = document.createElement('img');
  el3.className = 'my-marker';
  el3.src = '../svg/pin.svg';
  el3.onclick = () => map.update({ location: { ...LOCATION3, duration: 400 } });
  map.addChild(new YMapMarker({ coordinates: LOCATION3.center }, el3));

  const el4 = document.createElement('img');
  el4.className = 'my-marker';
  el4.src = '../svg/pin.svg';
  el4.onclick = () => map.update({ location: { ...LOCATION4, duration: 400 } });
  map.addChild(new YMapMarker({ coordinates: LOCATION4.center }, el4));
  ymaps3.ready.then(() => {
    changeCenter1.onclick = function () { setCenter(LOCATION1) };
    changeCenter2.onclick = function () { setCenter(LOCATION2) };
    changeCenter3.onclick = function () { setCenter(LOCATION3) };
    changeCenter4.onclick = function () { setCenter(LOCATION4) };
  });
}

const toggle = {
  location: true,
  fly: null
};

async function setCenter(NEW_LOCATION) {
  await ymaps3.ready;

  const LOCATION = { center: [27.610120723557344, 53.884848342819005], zoom: 13 };

  map.setLocation({
    ...(toggle.location ? NEW_LOCATION : LOCATION),
    duration: 1000
  });
  toggle.location = !toggle.location;
}

